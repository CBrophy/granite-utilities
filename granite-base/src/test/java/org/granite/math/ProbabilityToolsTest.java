package org.granite.math;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProbabilityToolsTest {

    @Test
    public void testIndependentUnion() {
        final ImmutableList<Double> test = ImmutableList.of(0.5, 0.4, 0.6);

        double result = ProbabilityTools.independentUnion(test);

        double expected = 0.5 + 0.4 + 0.6 - (0.5 * 0.4) - (0.5 * 0.6) - (0.4 * 0.6) + (0.5 * 0.4 * 0.6);

        assertEquals(expected, result, 0.01);

        double singularResult = ProbabilityTools.independentUnion(ImmutableList.of(0.5));

        assertEquals(0.5, singularResult, 0.001);

        double addAZeroResult = ProbabilityTools.independentUnion(ImmutableList.of(0.5, 0.0));

        assertEquals(0.5, addAZeroResult, 0.001);

    }

}