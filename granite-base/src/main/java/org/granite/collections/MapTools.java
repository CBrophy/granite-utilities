package org.granite.collections;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapTools {

    public static <K> void updateFrequencyMap(final Iterable<K> keys, final Map<K, Integer> frequencyMap, final int increment) {
        checkNotNull(keys, "keys");
        checkNotNull(frequencyMap, "frequencyMap");

        keys
                .forEach(key -> frequencyMap.merge(key, increment, (v1, v2) -> v1 + v2));
    }

    public static <K> void updateFrequencyMap(final Map<K, Integer> sourceFrequencyMap, final Map<K, Integer> destinationFrequencyMap) {
        checkNotNull(sourceFrequencyMap, "sourceFrequencyMap");
        checkNotNull(destinationFrequencyMap, "destinationFrequencyMap");

        sourceFrequencyMap
                .entrySet()
                .forEach(entry -> destinationFrequencyMap.merge(entry.getKey(), entry.getValue(), (v1, v2) -> v1 + v2));
    }
}
