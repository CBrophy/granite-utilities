package org.granite.collections;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class MapToolsTest {

    @Test
    public void testUpdateFrequencyMap() {
        final HashMap<String, Integer> testMap = new HashMap<>();
        testMap.put("1", 1);

        MapTools.updateFrequencyMap(ImmutableList.of("1", "2"), testMap, 2);

        assertEquals(3, (int) testMap.get("1"));
        assertEquals(2, (int) testMap.get("2"));

        final HashMap<String, Integer> testMap2 = new HashMap<>();
        testMap2.put("1", 1);
        testMap2.put("3", 5);

        MapTools.updateFrequencyMap(testMap, testMap2);

        assertEquals(4, (int) testMap2.get("1"));
        assertEquals(2, (int) testMap2.get("2"));
        assertEquals(5, (int) testMap2.get("3"));

    }

}