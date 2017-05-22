package org.granite.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorToolsTest {

  @Test
  public void testDotProduct() throws Exception {
    double[] vector1 = new double[]{1, 2, 3};
    double[] vector2 = new double[]{2, 3, 4};

    double expected = 1.0 * 2.0 + 2.0 * 3.0 + 3.0 * 4.0;

    assertEquals(expected, VectorTools.dotProduct(vector1, vector2), 0.0);
  }

  @Test
  public void testNorm() {
    double[] vector1 = new double[]{1, 2, 3};

    double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0 + 3.0 * 3.0);

    assertEquals(expected, VectorTools.norm(vector1), 0.0);
  }

  @Test
  public void testCosine() {

    double[] vector1 = new double[]{1, 2, 3};
    double[] vector2 = new double[]{2, 3, 4};
    double expectedDotProduct = 1.0 * 2.0 + 2.0 * 3.0 + 3.0 * 4.0;
    double expectedNorm1 = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0 + 3.0 * 3.0);
    double expectedNorm2 = Math.sqrt(2.0 * 2.0 + 3.0 * 3.0 + 4.0 * 4.0);

    assertEquals(expectedDotProduct / (expectedNorm1 * expectedNorm2),
        VectorTools.cosine(vector1, vector2), 0.0);
  }

  @Test
  public void testDistanceVector() {
    double[] vector1 = new double[]{1, 2, 3};
    double[] vector2 = new double[]{2, 3, 4};

    double[] expected = new double[]{1.0 - 2.0, 2.0 - 3.0, 3.0 - 4.0};

    double[] distance = VectorTools.distanceVector(vector1, vector2);

    for (int i = 0; i < vector1.length; i++) {
      assertEquals(expected[i], distance[i], 0.0);
    }
  }

  @Test
  public void testEuclideanDistance() {
    double[] center = new double[]{0, 0};

    double[] v1 = new double[]{1, 1};
    double[] v2 = new double[]{1, 0};
    double[] v3 = new double[]{1, -1};
    double[] v4 = new double[]{0, -1};
    double[] v5 = new double[]{-1, -1};
    double[] v6 = new double[]{-1, 0};
    double[] v7 = new double[]{-1, 1};
    double[] v8 = new double[]{0, 1};

    assertEquals(hypotenuse(center[0], v1[0], center[1], v1[1]),
        VectorTools.euclideanDistance(center, v1), 0.01);
    assertEquals(hypotenuse(center[0], v2[0], center[1], v2[1]),
        VectorTools.euclideanDistance(center, v2), 0.01);
    assertEquals(hypotenuse(center[0], v3[0], center[1], v3[1]),
        VectorTools.euclideanDistance(center, v3), 0.01);
    assertEquals(hypotenuse(center[0], v4[0], center[1], v4[1]),
        VectorTools.euclideanDistance(center, v4), 0.01);
    assertEquals(hypotenuse(center[0], v5[0], center[1], v5[1]),
        VectorTools.euclideanDistance(center, v5), 0.01);
    assertEquals(hypotenuse(center[0], v6[0], center[1], v6[1]),
        VectorTools.euclideanDistance(center, v6), 0.01);
    assertEquals(hypotenuse(center[0], v7[0], center[1], v7[1]),
        VectorTools.euclideanDistance(center, v7), 0.01);
    assertEquals(hypotenuse(center[0], v8[0], center[1], v8[1]),
        VectorTools.euclideanDistance(center, v8), 0.01);

  }

  private static double hypotenuse(double x1, double x2, double y1, double y2) {
    // Euclidean distance in two dimensions is the length of the
    // hypotenuse between the two points
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
  }
}