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

import static com.google.common.base.CharMatcher.inRange;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

      final List<String> lineParts = new ArrayList<>();

      int firstIndex = keyValueDelimiter.indexIn(trimmed);

      if (firstIndex >= 0) {

        if (firstIndex > 0) {
          // add first half
          lineParts.add(trimmed.substring(0, firstIndex));
        }

        if (firstIndex < trimmed.length() - 1) {
          // add second half
          lineParts.add(trimmed.substring(firstIndex + 1, trimmed.length()));
        }
      }

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
    return inRange('a', 'z')
        .or(inRange('A', 'Z'))
        .negate()
        .collapseFrom(wildText
            .toLowerCase(), ' ').trim();
  }

  public static int levenshtein(
      final String first,
      final String second) {
    checkNotNull(first, "first");
    checkNotNull(second, "second");

    int[][] distance = new int[first.length() + 1][second.length() + 1];

    if (Math.min(first.length(), second.length()) == 0) {
      return Math.abs(first.length() - second.length());
    }

    for (int i = 0; i <= first.length(); i++) {
      for (int j = 0; j <= second.length(); j++) {
        if (i == 0) {
          distance[i][j] = j;
        } else if (j == 0) {
          distance[i][j] = i;
        } else {
          distance[i][j] = min(distance[i - 1][j - 1]
                  + costOfSubstitution(first.charAt(i - 1), second.charAt(j - 1)),
              distance[i - 1][j] + 1,
              distance[i][j - 1] + 1);
        }
      }
    }

    return distance[first.length()][second.length()];
  }

  private static int costOfSubstitution(char a, char b) {
    return a == b ? 0 : 1;
  }

  private static int min(int... numbers) {
    return Arrays.stream(numbers)
        .min().orElse(Integer.MAX_VALUE);
  }

  public static List<String> textQualifiedStringSplit(
      final String line,
      final CharMatcher delimiter,
      final CharMatcher textQualifier,
      final boolean trimResults
  ) {
    checkNotNull(line, "line");
    checkNotNull(delimiter, "delimiter");
    checkNotNull(textQualifier, "textQualifier");

    if (textQualifier.removeFrom(line).trim().isEmpty()) {
      return new ArrayList<>();
    }

    final List<String> result = new ArrayList<>();

    int start = 0;
    boolean qualified = false;

    int index = 0;
    while (index < line.length()) {
      char current = line.charAt(index);

      if (index == 0 && delimiter.matches(current) ) {
        //edge case: first character is the delimiter
        result.add("");
      }
        //edge case: end of string
      if (index == line.length() - 1) {

        checkState(!textQualifier.matches(current) || qualified,"Unterminated qualifier in %s",line);

        if (delimiter.matches(current)) {
          //edge case: last character is the delimiter
          result.add("");
        } else {
          result.add(
              clean(line.substring(start), textQualifier, trimResults)
          );
        }
        break;
      }

      if (textQualifier.matches(current)) {
        qualified = !qualified;
      }

      if (delimiter.matches(current) && !qualified) {
        result.add(
            clean(line.substring(start, index), textQualifier, trimResults)
        );
        start = index + 1;
      }

      index++;
    }

    return result;
  }

  private static String clean(
      final String value,
      final CharMatcher textQualifier,
      final boolean trimResults){
    return trimResults ? textQualifier.removeFrom(value).trim() : textQualifier.removeFrom(value);
  }

}
