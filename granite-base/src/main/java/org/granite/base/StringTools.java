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
package org.granite.base;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public final class StringTools implements Serializable {

    private StringTools() {
    }

    public static boolean isNullOrEmpty(final String value) {
        return value == null || value.trim().isEmpty();
    }

    public static Map<String, String> convertStringsToMap(
            final List<String> lines,
            final CharMatcher keyValueDelimiter,
            final boolean omitEmptyValues) {

        checkNotNull(keyValueDelimiter, "keyValueDelimiter");

        if (lines == null || lines.isEmpty()) return ImmutableMap.of();

        final Splitter equalsSplitter = Splitter.on(keyValueDelimiter).trimResults();

        final HashMap<String, String> result = new HashMap<>();

        for (String line : lines) {

            final String trimmed = line.trim();

            if(trimmed.isEmpty()) continue;

            // Lines are expected to have a maximum of 1 delimiter
            // i.e. this = that where '=' is the delimiter
            // such that lineParts should never be more than 2 units in size
            final List<String> lineParts = equalsSplitter.splitToList(trimmed);

            checkState(lineParts.size() <= 2, "Too many %s delimiters on line: %s", keyValueDelimiter, line);

            if (lineParts.size() == 0 || (lineParts.size() == 1 && omitEmptyValues)) {
                continue;
            }

            // Duplicates will be overwritten
            result.put(lineParts.get(0), lineParts.size() == 1 ? "" : lineParts.get(1));

        }

        return ImmutableMap.copyOf(result);

    }
}
