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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class StatsTools {
    public static ImmutableMap<Integer, double[]> CHI_SQUARE_DISTRIBUTION = getChiSquareDistribution();
    public static double[] P_VALUES = new double[]{0.995, 0.99, 0.975, 0.95, 0.90, 0.10, 0.05, 0.025, 0.01, 0.005};

    public static double mean(final Iterable<? extends Number> values) {
        checkNotNull(values, "values");

        double sum = 0.0;
        int count = 0;

        for (Number value : values) {
            sum += value.doubleValue();
            count++;
        }

        checkState(count > 0, "Cannot find the mean on empty list");

        return sum / (double) count;
    }

    public static double variance(final Iterable<? extends Number> values) {
        return variance(values, mean(values));
    }

    public static double variance(final Iterable<? extends Number> values, final double mean) {
        checkNotNull(values, "values");

        double squareDiffSum = 0.0;
        int count = 0;

        for (Number value : values) {
            count++;
            squareDiffSum += Math.pow(mean - value.doubleValue(), 2);
        }

        checkState(count > 0, "Cannot determine variance on an empty list");

        return squareDiffSum / ((double) count - 1.0);

    }

    public static double standardDev(final Iterable<? extends Number> values) {
        return standardDev(variance(values));
    }

    public static double standardDev(final Iterable<? extends Number> values, final double mean) {
        return standardDev(variance(values, mean));
    }

    public static double standardDev(final double variance) {
        return Math.sqrt(variance);
    }

    public static double standardError(final Iterable<? extends Number> values) {
        return standardError(Iterables.size(values), variance(values));
    }

    public static double standardError(final int count, final double variance) {
        return Math.sqrt(variance / (double) count);
    }

    public static double meanSquareError(final List<? extends Number> predictions, final List<? extends Number> observations) {
        checkNotNull(predictions, "predictions");
        checkNotNull(observations, "observations");

        checkArgument(predictions.size() == observations.size(), "Prediction & observation collections must be of the same size");

        if (predictions.isEmpty()) {
            return Double.NaN;
        }

        double squareSum = 0.0;

        for (int index = 0; index < predictions.size(); index++) {
            final double prediction = predictions.get(index).doubleValue();
            final double observation = observations.get(index).doubleValue();
            squareSum += Math.pow(prediction - observation, 2);
        }

        return squareSum / (double) predictions.size();
    }

    public static double chiSquare(final List<? extends Number> predictions, final List<? extends Number> observations) {
        checkNotNull(predictions, "predictions");
        checkNotNull(observations, "observations");

        checkArgument(predictions.size() == observations.size(), "Prediction & observation collections must be of the same size");

        if (predictions.isEmpty()) {
            return Double.NaN;
        }

        double chiSquareSum = 0.0;

        for (int index = 0; index < predictions.size(); index++) {

            final double prediction = predictions.get(index).doubleValue();

            final double observation = observations.get(index).doubleValue();

            chiSquareSum += Math.pow(observation - prediction, 2) / prediction;
        }

        return chiSquareSum;

    }

    public static double pValue(final List<? extends Number> predictions, final List<? extends Number> observations, final int degreesOfFreedom) {
        return pValue(chiSquare(predictions, observations), degreesOfFreedom);
    }

    public static double pValue(final double chiSquare, final int degreesOfFreedom) {
        checkArgument(CHI_SQUARE_DISTRIBUTION.containsKey(degreesOfFreedom), "degreesOfFreedom must be a key in CHI_SQUARE_DISTRIBUTION table");

        final double[] chiSquares = CHI_SQUARE_DISTRIBUTION.get(degreesOfFreedom);

        checkNotNull(chiSquares, "chiSquares");

        for (int index = 0; index < chiSquares.length; index++) {

            if (chiSquares[index] > chiSquare) {
                return P_VALUES[index];
            }
        }

        return P_VALUES[P_VALUES.length - 1];
    }

    public static double rootMeanSquareError(final List<? extends Number> predictions, final List<? extends Number> observations) {
        return Math.sqrt(meanSquareError(predictions, observations));
    }

    private static ImmutableMap<Integer, double[]> getChiSquareDistribution() {
        final ImmutableMap.Builder<Integer, double[]> distributionTable = ImmutableMap.builder();

        distributionTable.put(1, new double[]{0, 0, 0.001, 0.004, 0.016, 2.706, 3.841, 5.024, 6.635, 7.879});
        distributionTable.put(2, new double[]{0.01, 0.02, 0.051, 0.103, 0.211, 4.605, 5.991, 7.378, 9.21, 10.597});
        distributionTable.put(3, new double[]{0.072, 0.115, 0.216, 0.352, 0.584, 6.251, 7.815, 9.348, 11.345, 12.838});
        distributionTable.put(4, new double[]{0.207, 0.297, 0.484, 0.711, 1.064, 7.779, 9.488, 11.143, 13.277, 14.86});
        distributionTable.put(5, new double[]{0.412, 0.554, 0.831, 1.145, 1.61, 9.236, 11.07, 12.833, 15.086, 16.75});
        distributionTable.put(6, new double[]{0.676, 0.872, 1.237, 1.635, 2.204, 10.645, 12.592, 14.449, 16.812, 18.548});
        distributionTable.put(7, new double[]{0.989, 1.239, 1.69, 2.167, 2.833, 12.017, 14.067, 16.013, 18.475, 20.278});
        distributionTable.put(8, new double[]{1.344, 1.646, 2.18, 2.733, 3.49, 13.362, 15.507, 17.535, 20.09, 21.955});
        distributionTable.put(9, new double[]{1.735, 2.088, 2.7, 3.325, 4.168, 14.684, 16.919, 19.023, 21.666, 23.589});
        distributionTable.put(10, new double[]{2.156, 2.558, 3.247, 3.94, 4.865, 15.987, 18.307, 20.483, 23.209, 25.188});
        distributionTable.put(11, new double[]{2.603, 3.053, 3.816, 4.575, 5.578, 17.275, 19.675, 21.92, 24.725, 26.757});
        distributionTable.put(12, new double[]{3.074, 3.571, 4.404, 5.226, 6.304, 18.549, 21.026, 23.337, 26.217, 28.3});
        distributionTable.put(13, new double[]{3.565, 4.107, 5.009, 5.892, 7.042, 19.812, 22.362, 24.736, 27.688, 29.819});
        distributionTable.put(14, new double[]{4.075, 4.66, 5.629, 6.571, 7.79, 21.064, 23.685, 26.119, 29.141, 31.319});
        distributionTable.put(15, new double[]{4.601, 5.229, 6.262, 7.261, 8.547, 22.307, 24.996, 27.488, 30.578, 32.801});
        distributionTable.put(16, new double[]{5.142, 5.812, 6.908, 7.962, 9.312, 23.542, 26.296, 28.845, 32, 34.267});
        distributionTable.put(17, new double[]{5.697, 6.408, 7.564, 8.672, 10.085, 24.769, 27.587, 30.191, 33.409, 35.718});
        distributionTable.put(18, new double[]{6.265, 7.015, 8.231, 9.39, 10.865, 25.989, 28.869, 31.526, 34.805, 37.156});
        distributionTable.put(19, new double[]{6.844, 7.633, 8.907, 10.117, 11.651, 27.204, 30.144, 32.852, 36.191, 38.582});
        distributionTable.put(20, new double[]{7.434, 8.26, 9.591, 10.851, 12.443, 28.412, 31.41, 34.17, 37.566, 39.997});
        distributionTable.put(21, new double[]{8.034, 8.897, 10.283, 11.591, 13.24, 29.615, 32.671, 35.479, 38.932, 41.401});
        distributionTable.put(22, new double[]{8.643, 9.542, 10.982, 12.338, 14.041, 30.813, 33.924, 36.781, 40.289, 42.796});
        distributionTable.put(23, new double[]{9.26, 10.196, 11.689, 13.091, 14.848, 32.007, 35.172, 38.076, 41.638, 44.181});
        distributionTable.put(24, new double[]{9.886, 10.856, 12.401, 13.848, 15.659, 33.196, 36.415, 39.364, 42.98, 45.559});
        distributionTable.put(25, new double[]{10.52, 11.524, 13.12, 14.611, 16.473, 34.382, 37.652, 40.646, 44.314, 46.928});
        distributionTable.put(26, new double[]{11.16, 12.198, 13.844, 15.379, 17.292, 35.563, 38.885, 41.923, 45.642, 48.29});
        distributionTable.put(27, new double[]{11.808, 12.879, 14.573, 16.151, 18.114, 36.741, 40.113, 43.195, 46.963, 49.645});
        distributionTable.put(28, new double[]{12.461, 13.565, 15.308, 16.928, 18.939, 37.916, 41.337, 44.461, 48.278, 50.993});
        distributionTable.put(29, new double[]{13.121, 14.256, 16.047, 17.708, 19.768, 39.087, 42.557, 45.722, 49.588, 52.336});
        distributionTable.put(30, new double[]{13.787, 14.953, 16.791, 18.493, 20.599, 40.256, 43.773, 46.979, 50.892, 53.672});
        distributionTable.put(40, new double[]{20.707, 22.164, 24.433, 26.509, 29.051, 51.805, 55.758, 59.342, 63.691, 66.766});
        distributionTable.put(50, new double[]{27.991, 29.707, 32.357, 34.764, 37.689, 63.167, 67.505, 71.42, 76.154, 79.49});
        distributionTable.put(60, new double[]{35.534, 37.485, 40.482, 43.188, 46.459, 74.397, 79.082, 83.298, 88.379, 91.952});
        distributionTable.put(70, new double[]{43.275, 45.442, 48.758, 51.739, 55.329, 85.527, 90.531, 95.023, 100.425, 104.215});
        distributionTable.put(80, new double[]{51.172, 53.54, 57.153, 60.391, 64.278, 96.578, 101.879, 106.629, 112.329, 116.321});
        distributionTable.put(90, new double[]{59.196, 61.754, 65.647, 69.126, 73.291, 107.565, 113.145, 118.136, 124.116, 128.299});
        distributionTable.put(100, new double[]{67.328, 70.065, 74.222, 77.929, 82.358, 118.498, 124.342, 129.561, 135.807, 140.169});

        return distributionTable.build();
    }
}
