package org.granite.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void testIntersectKeysCaseInsensitive() {
        final ImmutableMap<String, Integer> map1 = ImmutableMap.of("hello", 1, "tEst", 2);
        final ImmutableMap<String, Integer> map2 = ImmutableMap.of("bye", 3, "TeSt", 4);

        final Map<String, String> intersection = MapTools.intersectKeysCaseInsensitive(map1,
                                                                                       map2);

        assertNotNull(intersection);
        assertEquals(1, intersection.size());

        assertEquals("TeSt", intersection.get("tEst"));

    }

}