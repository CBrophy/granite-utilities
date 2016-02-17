package org.granite.math;

import org.junit.Test;

import static org.junit.Assert.*;

public class VectorToolsTest {

    @Test
    public void testDotProduct() throws Exception {
        double[] vector1 = new double[]{1,2,3};
        double[] vector2 = new double[]{2,3,4};

        double expected = 1.0 * 2.0 + 2.0 * 3.0 + 3.0 * 4.0;

        assertEquals(expected, VectorTools.dotProduct(vector1, vector2), 0.0);
    }

    @Test
    public void testNorm(){
        double[] vector1 = new double[]{1,2,3};

        double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0 + 3.0 * 3.0);

        assertEquals(expected, VectorTools.norm(vector1), 0.0);
    }

    @Test
    public void testCosine(){

        double[] vector1 = new double[]{1,2,3};
        double[] vector2 = new double[]{2,3,4};
        double expectedDotProduct = 1.0 * 2.0 + 2.0 * 3.0 + 3.0 * 4.0;
        double expectedNorm1 = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0 + 3.0 * 3.0);
        double expectedNorm2 = Math.sqrt(2.0 * 2.0 + 3.0 * 3.0 + 4.0 * 4.0);

        assertEquals(expectedDotProduct / (expectedNorm1 * expectedNorm2), VectorTools.cosine(vector1, vector2), 0.0);
    }
}