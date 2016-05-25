package org.granite.math;

import com.google.common.collect.ImmutableSortedMap;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CDFToolsTest {

    @Test
    public void testGeneratePercentageCDF() throws Exception {

        final LocalDateTime start = LocalDateTime.of(2016, 5, 24, 0, 0);
        final LocalDateTime end = LocalDateTime.of(2016, 5, 25, 0, 0);
        final int minutesPerEpoch = 60;

        CDFTools<Integer> cdfTools = new CDFTools<>(minutesPerEpoch, start, end);

        cdfTools.add(LocalDateTime.of(2016, 5, 24, 1, 0), 5);
        cdfTools.add(LocalDateTime.of(2016, 5, 24, 2, 0), 5);
        cdfTools.add(LocalDateTime.of(2016, 5, 24, 3, 0), 5);
        cdfTools.add(LocalDateTime.of(2016, 5, 24, 4, 0), 5);
        cdfTools.add(LocalDateTime.of(2016, 5, 24, 5, 0), 5);

        assertEquals(24, cdfTools.getTotalEpochs());

        final ImmutableSortedMap<Integer, Double> percentageCDF = cdfTools.generateDecimalCDF();

        assertEquals(24, percentageCDF.size());

        assertEquals(0.0, percentageCDF.get(0), 0.0001);
        assertEquals(0.2, percentageCDF.get(1), 0.0001);
        assertEquals(0.4, percentageCDF.get(2), 0.0001);
        assertEquals(0.6, percentageCDF.get(3), 0.0001);
        assertEquals(0.8, percentageCDF.get(4), 0.0001);
        assertEquals(1.0, percentageCDF.get(5), 0.0001);

        for (int epoch = 6; epoch < 24; epoch++) {
            assertEquals(1.0, percentageCDF.get(epoch), 0.0001);
        }

    }
}