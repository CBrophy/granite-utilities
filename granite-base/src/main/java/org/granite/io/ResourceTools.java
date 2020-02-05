/*
 * Copyright (C) 2016 Charles Brophy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.granite.io;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import org.granite.base.ExceptionTools;
import org.granite.base.StringTools;
import org.granite.log.LogTools;

public final class ResourceTools implements Serializable {

  private ResourceTools() {
  }

  @SuppressWarnings("UnstableApiUsage")
  public static InputStream readResource(final String resourceName) {
    checkNotNull(resourceName, "resourceName");

    try {

      final URL resourceUrl = Resources.getResource(resourceName);

      checkNotNull(resourceUrl, "Unknown resource: %s", resourceName);

      return Resources.asByteSource(resourceUrl).openStream();
    } catch (IOException e) {
      throw ExceptionTools.checkedToRuntime(e);
    } catch (IllegalArgumentException ignored) {
      LogTools.warn("Resource {0} does not exist", resourceName);
    }

    return null;
  }

  public static <T> List<T> readResourceTextFile(
      final String resourceName,
      final int skipRows,
      final Function<String, Boolean> rowFilter,
      final Function<String, T> rowDeserializer) {

    checkNotNull(resourceName, "resourceName");
    checkArgument(resourceName.trim().length() > 0, "Resource name is empty");
    checkNotNull(rowDeserializer, "rowDeserializer");
    checkNotNull(rowFilter, "rowFilter");

    final ImmutableList.Builder<T> resultBuilder = ImmutableList.builder();

    final InputStream resourceStream = ResourceTools.readResource(resourceName);

    if(resourceStream != null) {

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(
          resourceName.endsWith(".gz") ? new GZIPInputStream(resourceStream) : resourceStream
      ))) {

        String line = null;

        int rowCount = 0;

        while ((line = reader.readLine()) != null) {
          if (rowCount >= skipRows) {

            if (rowFilter.apply(line)) {
              resultBuilder.add(rowDeserializer.apply(line));
            }
          }

          rowCount++;
        }

      } catch (IOException e) {
        throw ExceptionTools.checkedToRuntime(e);
      }
    } else {
      LogTools.warn("Could not find embedded resource: {0}", resourceName);
    }

    return resultBuilder.build();
  }

  public static List<String> readResourceTextFile(final String resourceName) {

    return readResourceTextFile(
        resourceName,
        0,
        row -> !StringTools.isNullOrEmpty(row),
        String::trim
    );
  }

  public static Map<String, String> readResourceTextFileAsMap(final String resourceName) {

    checkNotNull(resourceName, "resourceName");

    return StringTools
        .convertStringsToMap(readResourceTextFile(
            resourceName,
            0,
            row -> !StringTools.isNullOrEmpty(row) && !row.trim().startsWith("#"),
            String::trim
            ), CharMatcher.is('='), false);

  }
}
