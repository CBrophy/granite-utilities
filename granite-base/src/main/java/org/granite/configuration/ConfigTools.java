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
package org.granite.configuration;

import com.google.common.base.CharMatcher;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import org.granite.base.StringTools;
import org.granite.io.FileTools;
import org.granite.io.ResourceTools;
import org.granite.log.LogTools;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ConfigTools implements Serializable {

    private ConfigTools() {
    }

    public static ApplicationConfiguration readConfiguration(final String configFile, final String... resources) {
        if (resources == null) return new ApplicationConfiguration(ImmutableMap.of());

        final HashMap<String, String> result = new HashMap<>();

        for (String resource : resources) {
            mergeConfigMaps(ResourceTools.readResourceTextFileAsMap(resource), result);
        }

        // Read anything from an external config file
        mergeConfigMaps(readConfigFileLines(configFile), result);

        // Check for system properties overrides
        // for the known configuration options and apply them
        // as overrides
        for (String configKey : result.keySet()) {
            final String overrideValue = System.getProperties().getProperty(configKey, "");

            if (!StringTools.isNullOrEmpty(overrideValue)) {
                result.put(configKey, overrideValue.trim());
            }
        }

        return new ApplicationConfiguration(ImmutableMap.copyOf(result));

    }

    private static Map<String, String> readConfigFileLines(final String configFile) {
        if (StringTools.isNullOrEmpty(configFile) || !FileTools.fileExistsAndCanRead(configFile))
            return ImmutableMap.of();

        try {
            LogTools.info("Reading config values from file {0}", configFile);

            return StringTools.convertStringsToMap(
                    Files.readLines(new File(configFile), Charset.defaultCharset())
                            .stream()
                            .filter(line -> !line.trim().startsWith("#")) // remove comments
                            .collect(Collectors.toList()),
                    CharMatcher.is('='),
                    false);

        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private static void mergeConfigMaps(final Map<String, String> source, final HashMap<String, String> destination) {
        checkNotNull(source, "source");
        checkNotNull(destination, "destination");

        if (source.isEmpty()) {
            return;
        }

        final Sets.SetView<String> sharedKeys = Sets.intersection(source.keySet(), destination.keySet());
        final Sets.SetView<String> newKeys = Sets.difference(source.keySet(), destination.keySet());

        // skip empty values in the source map
        // if they are already in the destination in order to
        // prevent overwrites of populated keys with empty ones
        sharedKeys
                .stream()
                .filter(key -> !source.get(key).trim().isEmpty() && !key.startsWith("#"))
                .forEach(key -> destination.put(key, source.get(key)));

        // Add new keys regardless of whether or not they're empty
        newKeys
                .stream()
                .filter(key -> !key.startsWith("#"))
                .forEach(key -> destination.put(key, source.get(key).trim()));
    }


}
