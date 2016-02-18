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
import com.google.common.collect.Ordering;
import com.google.common.math.DoubleMath;

import java.io.Serializable;
import java.util.List;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class PercentileTools implements Serializable {

    public static double[] findQuantiles(final List<? extends Number> sortedValues, final int quantileCount) {
        checkNotNull(sortedValues, "sortedValues");

        if (sortedValues.isEmpty()) return new double[]{};

        checkArgument(quantileCount > 1 && sortedValues.size() >= quantileCount, "quantileCount must be greater than 1 and smaller than the number of elements");

        final double[] result = new double[quantileCount - 1];

        // 2 quantiles = .5
        // 3 quantiles = .33
        // 4 quantiles = .25
        // ...etc
        double phi = 1.0 / (double) quantileCount;

        for (int currentQuantile = 1; currentQuantile < quantileCount; currentQuantile++) {

            final double index = phi * (double) currentQuantile * (sortedValues.size() + 1);

            final int indexInteger = (int) index;

            if (index - indexInteger == 0.0) { // it's a whole number, so just return the value at the zero-based index
                result[currentQuantile - 1] = sortedValues.get(indexInteger - 1).doubleValue();
            } else {
                result[currentQuantile - 1] = DoubleMath.mean(sortedValues.get(indexInteger - 1).doubleValue(), sortedValues.get(indexInteger).doubleValue());

            }

        }

        return result;
    }

    public static double findPercentile(final double value, final double[] quantiles) {
        checkNotNull(quantiles, "quantiles");
        checkArgument(quantiles.length > 0, "quantiles must contain at least one value");

        final double rank = 1.0 / (quantiles.length + 1.0);

        double percentile = rank;

        for (int index = 0; index < quantiles.length; index++) {

            if (value < quantiles[index]) {
                return percentile;
            }

            percentile += rank;

        }

        return percentile;

    }

    public static <T extends Comparable<T>> ImmutableMap<T, Double> findPercentiles(final Iterable<T> items, final int precision, final boolean complement) {
        checkNotNull(items, "items");

        final TreeSet<T> sortedItems = new TreeSet<>(complement ? Ordering.natural().reverse() : Ordering.natural());

        items.forEach(sortedItems::add);

        if (sortedItems.isEmpty()) return ImmutableMap.of();

        final double n = sortedItems.size();

        int position = 1;

        final ImmutableMap.Builder<T, Double> result = ImmutableMap.builder();

        while (!sortedItems.isEmpty()) {
            final T item = sortedItems.pollFirst();
            final double percentile = MathTools.round((double) position / n, precision);
            result.put(item, percentile);
            position++;
        }

        return result.build();

    }


}
