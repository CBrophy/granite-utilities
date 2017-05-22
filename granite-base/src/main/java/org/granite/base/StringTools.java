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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class StringTools implements Serializable {

  private StringTools() {
  }

  public static boolean isNullOrEmpty(final String value) {
    return value == null || value.trim().isEmpty();
  }

  public static boolean anyNullOrEmpty(final String... values) {
    if (values == null) {
      return true;
    }

    for (String value : values) {
      if (isNullOrEmpty(value)) {
        return true;
      }
    }

    return false;
  }

  public static Map<String, String> convertStringsToMap(
      final String lines,
      final CharMatcher entryDelimiter,
      final CharMatcher keyValueDelimiter,
      final boolean omitEmptyValues
  ) {
    return convertStringsToMap(
        lines,
        entryDelimiter,
        keyValueDelimiter,
        value -> value,
        omitEmptyValues
    );
  }

  public static Map<String, String> convertStringsToMap(
      final List<String> lines,
      final CharMatcher keyValueDelimiter,
      final boolean omitEmptyValues
  ) {
    return convertStringsToMap(
        lines,
        keyValueDelimiter,
        value -> value,
        omitEmptyValues
    );
  }

  public static <V> Map<String, V> convertStringsToMap(
      final String lines,
      final CharMatcher entryDelimiter,
      final CharMatcher keyValueDelimiter,
      final Function<String, V> valueConverter,
      final boolean omitEmptyValues) {
    if (StringTools.isNullOrEmpty(lines)) {
      return ImmutableMap.of();
    }

    checkNotNull(entryDelimiter, "entryDelimiter");

    return convertStringsToMap(
        Splitter.on(entryDelimiter)
            .trimResults()
            .omitEmptyStrings()
            .splitToList(lines),
        keyValueDelimiter,
        valueConverter,
        omitEmptyValues
    );
  }

  public static <V> Map<String, V> convertStringsToMap(
      final List<String> lines,
      final CharMatcher keyValueDelimiter,
      final Function<String, V> valueConverter,
      final boolean omitEmptyValues) {

    checkNotNull(keyValueDelimiter, "keyValueDelimiter");
    checkNotNull(valueConverter, "valueConverter");

    if (lines == null || lines.isEmpty()) {
      return ImmutableMap.of();
    }

    final Splitter keyValueSplitter = Splitter.on(keyValueDelimiter).trimResults();

    final HashMap<String, V> result = new HashMap<>();

    for (String line : lines) {

      final String trimmed = line.trim();

      if (trimmed.isEmpty()) {
        continue;
      }

      // Lines are expected to have a maximum of 1 delimiter
      // i.e. this = that where '=' is the delimiter
      // such that lineParts should never be more than 2 units in size
      final List<String> lineParts = keyValueSplitter
          .trimResults()
          .omitEmptyStrings()
          .splitToList(trimmed);

      checkState(lineParts.size() <= 2,
          "Too many %s delimiters on line: %s",
          keyValueDelimiter,
          line);

      if (lineParts.size() == 0 || (lineParts.size() == 1 && omitEmptyValues)) {
        continue;
      }

      final V value = valueConverter.apply(
          lineParts.size() == 1 ? "" : lineParts.get(1)
      );

      checkArgument(value != null, "Failed to convert map for k/v pair: %s", line);

      // Duplicates will be overwritten
      result.put(lineParts.get(0), value);

    }

    return ImmutableMap.copyOf(result);

  }

  public static String truncate(final String value, final int maxLength) {
    checkNotNull(value, "value");
    checkArgument(maxLength > 0, "maxLength must be a positive number");

    return value.length() <= maxLength ? value : value.substring(0, maxLength);
  }

  public static String cleanSentence(final String... text) {
    checkNotNull(text, "text");

    if (text.length == 0) {
      return "";
    }

    return cleanText(Joiner.on(' ').skipNulls().join(text));
  }

  public static String cleanText(final String wildText) {
    checkNotNull(wildText, "wildText");

    if (wildText.isEmpty()) {
      return "";
    }

    // Turn everything that is not a lower-case letter or number
    // into a space and collapse any groups to a single space
    return CharMatcher
        .javaLetter()
        .negate()
        .collapseFrom(wildText
            .toLowerCase(), ' ').trim();
  }

}
