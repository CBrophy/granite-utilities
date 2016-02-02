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
package org.granite.math;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PercentileToolsTest {

    private final static List<Double> testList1 = ImmutableList.of(0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8);
    private final static List<Double> testList2 = ImmutableList.of(0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9);
    private static final ImmutableList<Integer> percentileRankList = ImmutableList.of(6, 9, 45, 45, 1000, -1);

    @Test
    public void testFindQuantiles() throws Exception {

        double[] medianOdd = PercentileTools.findQuantiles(testList1, 2);

        assertEquals(0.5, medianOdd[0], 0.0);

        double[] medianEven = PercentileTools.findQuantiles(testList2, 2);

        assertEquals(0.55, medianEven[0], 0.0);

        double[] quartilesOdd = PercentileTools.findQuantiles(testList1, 4);

        assertEquals(0.3, quartilesOdd[0], 0.0);
        assertEquals(0.5, quartilesOdd[1], 0.0);
        assertEquals(0.7, quartilesOdd[2], 0.0);

        double[] quartilesEven = PercentileTools.findQuantiles(testList2, 4);

        assertEquals(0.35, quartilesEven[0], 0.0);
        assertEquals(0.55, quartilesEven[1], 0.0);
        assertEquals(0.75, quartilesEven[2], 0.0);

    }

    @Test
    public void testFindPercentile(){
        double[] quartilesOdd = PercentileTools.findQuantiles(testList1, 4);
        double[] quartilesEven = PercentileTools.findQuantiles(testList2, 4);

        assertEquals(0.25, PercentileTools.findPercentile(0.15, quartilesEven), 0.0);
        assertEquals(0.25, PercentileTools.findPercentile(0.15, quartilesOdd), 0.0);

        assertEquals(0.5, PercentileTools.findPercentile(0.37, quartilesEven), 0.0);
        assertEquals(0.5, PercentileTools.findPercentile(0.47, quartilesOdd), 0.0);

        assertEquals(0.75, PercentileTools.findPercentile(0.57, quartilesEven), 0.0);
        assertEquals(0.75, PercentileTools.findPercentile(0.67, quartilesOdd), 0.0);

        assertEquals(1.0, PercentileTools.findPercentile(0.99, quartilesEven), 0.0);
        assertEquals(1.0, PercentileTools.findPercentile(0.99, quartilesOdd), 0.0);
    }

    @Test
    public void testPercentileRank() throws Exception {

        final ImmutableMap<Integer, Double> rankMap = PercentileTools.findPercentiles(percentileRankList, 2, false);
        final ImmutableMap<Integer, Double> rankMapComplement = PercentileTools.findPercentiles(percentileRankList, 2, true);

        assertEquals(5, rankMap.size());
        assertEquals(rankMap.size(), rankMapComplement.size());

        assertEquals(0.2, rankMap.get(-1), 0.0);
        assertEquals(0.4, rankMap.get(6), 0.0);
        assertEquals(0.6, rankMap.get(9), 0.0);
        assertEquals(0.8, rankMap.get(45), 0.0);
        assertEquals(1.0, rankMap.get(1000), 0.0);

        assertEquals(1.0, rankMapComplement.get(-1), 0.0);
        assertEquals(0.8, rankMapComplement.get(6), 0.0);
        assertEquals(0.6, rankMapComplement.get(9), 0.0);
        assertEquals(0.4, rankMapComplement.get(45), 0.0);
        assertEquals(0.2, rankMapComplement.get(1000), 0.0);

    }
}