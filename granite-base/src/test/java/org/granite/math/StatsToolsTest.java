package org.granite.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

public class StatsToolsTest {


  @Test
  public void testMean() {
    double mean1 = StatsTools.mean(ImmutableList.of(1, 2));
    double mean2 = StatsTools.mean(ImmutableList.of(5));
    double mean3 = StatsTools.mean(ImmutableList.of(1, 2, 3));

    assertEquals(1.5, mean1, 0.0);
    assertEquals(5.0, mean2, 0.0);
    assertEquals(2.0, mean3, 0.0);
  }

  @Test
  public void testVariance() {
    final ImmutableList<Integer> values = ImmutableList.of(20, 12, 30);

    double mean = (20.0 + 12.0 + 30.0) / 3.0;
    double variance = StatsTools.variance(values, mean);
    double expected =
        (Math.pow(20.0 - mean, 2) + Math.pow(12.0 - mean, 2) + Math.pow(30.0 - mean, 2)) / 2.0;
    assertEquals(expected, variance, 0.001);
  }

  @Test
  public void testStandardDeviation() {
    final ImmutableList<Integer> values = ImmutableList.of(20, 12, 30);

    double mean = (20.0 + 12.0 + 30.0) / 3.0;
    double variance =
        (Math.pow(20.0 - mean, 2) + Math.pow(12.0 - mean, 2) + Math.pow(30.0 - mean, 2)) / 2.0;
    double expected = Math.sqrt(variance);
    assertEquals(expected, StatsTools.standardDev(values), 0.001);
  }

  @Test
  public void testStandardError() {
    final ImmutableList<Integer> values = ImmutableList.of(20, 12, 30);

    double mean = (20.0 + 12.0 + 30.0) / 3.0;
    double variance =
        (Math.pow(20.0 - mean, 2) + Math.pow(12.0 - mean, 2) + Math.pow(30.0 - mean, 2)) / 2.0;
    double expected = Math.sqrt(variance / 3.0);

    assertEquals(expected, StatsTools.standardError(3, StatsTools.variance(values)), 0.001);
  }

  @Test
  public void testMeanSquareError() {
    final ImmutableList<Double> observed = ImmutableList.of(25.0, 65.0);
    final ImmutableList<Double> predicted = ImmutableList.of(30.0, 50.0);

    double expected = (Math.pow(25.0 - 30.0, 2) + Math.pow(65.0 - 50.0, 2)) / 2;

    assertEquals(expected, StatsTools.meanSquareError(predicted, observed), 0.001);
  }

  @Test
  public void testRMSE() {
    final ImmutableList<Double> observed = ImmutableList.of(25.0, 65.0);
    final ImmutableList<Double> predicted = ImmutableList.of(30.0, 50.0);

    double expected = Math.sqrt((Math.pow(25.0 - 30.0, 2) + Math.pow(65.0 - 50.0, 2)) / 2);

    assertEquals(expected, StatsTools.rootMeanSquareError(predicted, observed), 0.001);
  }

  @Test
  public void testChiSquare() {
    final ImmutableList<Double> observed = ImmutableList.of(25.0, 65.0);
    final ImmutableList<Double> predicted = ImmutableList.of(30.0, 50.0);

    double expected = (Math.pow(25.0 - 30.0, 2) / 30.0) + (Math.pow(65.0 - 50.0, 2) / 50.0);

    assertEquals(expected, StatsTools.chiSquare(predicted, observed), 0.001);
  }

  @Test
  public void testPValue() {
    final ImmutableList<Double> observed = ImmutableList.of(25.0, 65.0);
    final ImmutableList<Double> predicted = ImmutableList.of(30.0, 50.0);

    double expected = 0.01;

    assertEquals(expected, StatsTools.pValue(predicted, observed, 1), 0.001);
  }

  @Test
  public void testChiSquareDistribution() {
    int pValueLength = StatsTools.P_VALUES.length;

    for (Integer degreesOfFreedom : StatsTools.CHI_SQUARE_DISTRIBUTION.keySet()) {

      final double[] chiSquares = StatsTools.CHI_SQUARE_DISTRIBUTION.get(degreesOfFreedom);

      assertNotNull(chiSquares);

      assertEquals(pValueLength, chiSquares.length);
    }
  }
}