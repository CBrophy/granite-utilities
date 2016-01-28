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

import com.google.common.base.CharMatcher;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

import org.granite.base.StringTools;
import org.granite.log.LogTools;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ResourceTools implements Serializable {

    private ResourceTools() {
    }

    public static InputStream readResource(final String resourceName) {
        checkNotNull(resourceName, "resourceName");

        try {

            final URL resourceUrl = Resources.getResource(resourceName);

            checkNotNull(resourceUrl, "Unknown resource: %s", resourceName);

            return Resources.asByteSource(resourceUrl).openStream();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        } catch (IllegalArgumentException ignored) {
            LogTools.warn("Resource {0} does not exist", resourceName);
        }

        return null;
    }

    public static List<String> readResourceTextFile(final String resourceName) {

        checkNotNull(resourceName, "resourceName");

        try {
            InputStream inputStream = readResource(resourceName);

            if (inputStream == null) {
                return ImmutableList.of();
            }

            LogTools.info("Reading from embedded resource: {0}", resourceName);

            if (resourceName.endsWith(".gz")) {
                inputStream = new GZIPInputStream(inputStream);
            }

            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            final ImmutableList.Builder<String> resultBuilder = ImmutableList.builder();

            while ((line = reader.readLine()) != null) {

                final String trimmed = line.trim();

                if (trimmed.length() > 0) {
                    resultBuilder.add(trimmed);
                }

            }

            return resultBuilder.build();

        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static Map<String, String> readResourceTextFileAsMap(final String resourceName) {

        checkNotNull(resourceName, "resourceName");

        return StringTools.convertStringsToMap(readResourceTextFile(resourceName), CharMatcher.is('='), false);

    }
}
