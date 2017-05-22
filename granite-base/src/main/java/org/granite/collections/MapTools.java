package org.granite.collections;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapTools {

  public static <K> void updateFrequencyMap(final Iterable<K> keys,
      final Map<K, Integer> frequencyMap,
      final int increment) {
    checkNotNull(keys, "keys");
    checkNotNull(frequencyMap, "frequencyMap");

    keys
        .forEach(key -> frequencyMap.merge(key, increment, (v1, v2) -> v1 + v2));
  }

  public static <K> void updateFrequencyMap(final Map<K, Integer> sourceFrequencyMap,
      final Map<K, Integer> destinationFrequencyMap) {
    checkNotNull(sourceFrequencyMap, "sourceFrequencyMap");
    checkNotNull(destinationFrequencyMap, "destinationFrequencyMap");

    sourceFrequencyMap
        .entrySet()
        .forEach(entry -> destinationFrequencyMap.merge(entry.getKey(),
            entry.getValue(),
            (v1, v2) -> v1 + v2));
  }

  public static <V1, V2> Map<String, String> intersectKeysCaseInsensitive(
      final Map<String, V1> map1,
      final Map<String, V2> map2
  ) {
    return intersectKeys(
        map1,
        map2,
        String::toLowerCase,
        String::toLowerCase
    );
  }

  public static <K1, V1, K2, V2, K3> Map<K1, K2> intersectKeys(
      final Map<K1, V1> map1,
      final Map<K2, V2> map2,
      final Function<K1, K3> key1Transform,
      final Function<K2, K3> key2Transform
  ) {
    checkNotNull(map1, "map1");
    checkNotNull(map2, "map2");
    checkNotNull(key1Transform, "key1Transform");
    checkNotNull(key2Transform, "key2Transform");

    if (map1.isEmpty() || map2.isEmpty()) {
      return ImmutableMap.of();
    }

    final Map<K3, K1> keys1 = map1
        .keySet()
        .stream()
        .collect(Collectors.toMap(key1Transform, key -> key));

    final Map<K3, K2> keys2 = map2
        .keySet()
        .stream()
        .collect(Collectors.toMap(key2Transform, key -> key));

    final HashMap<K1, K2> result = new HashMap<>();

    for (Map.Entry<K3, K1> keys1Entry : keys1.entrySet()) {

      final K2 key2Key = keys2.get(keys1Entry.getKey());

      if (key2Key != null) {
        result.put(keys1Entry.getValue(), key2Key);
      }

    }

    return result;
  }

  public static <K, V> void printMap(final Map<K, V> sourceMap) {
    if (sourceMap == null) {
      return;
    }

    sourceMap
        .entrySet()
        .stream()
        .map(entry -> String.valueOf(entry.getKey()) + " -> " + String.valueOf(entry.getValue()))
        .forEach(System.out::println);
  }
}
