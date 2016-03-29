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

import com.google.common.math.DoubleMath;

import java.io.Serializable;
import java.math.RoundingMode;

import static com.google.common.base.Preconditions.checkArgument;

public final class MathTools implements Serializable {

    private MathTools() {
    }

    public static double exponentialDecay(final double value, final int time, final double decayCoefficient) {
        return value * Math.exp(time * decayCoefficient);
    }

    public static double exponentialDecay(final double value, final long time, final double decayCoefficient) {
        return value * Math.exp(time * decayCoefficient);
    }

    public static double round(final double value, final int precision) {
        final double coefficient = Math.pow(10.0, precision);

        return DoubleMath.roundToLong(value * coefficient, RoundingMode.HALF_UP) / coefficient;
    }

    public static double minMaxBound(final double value, final double min, final double max) {
        checkArgument(min < max, "min must be less than max");
        return Math.max(Math.min(value, max), min);
    }

    public static double minMaxBound(final int value, final int min, final int max) {
        checkArgument(min < max, "min must be less than max");
        return Math.max(Math.min(value, max), min);
    }

    public static double minMaxBound(final long value, final long min, final long max) {
        checkArgument(min < max, "min must be less than max");
        return Math.max(Math.min(value, max), min);
    }

    public static boolean isBetween(final double value, final double min, final double max) {
        checkArgument(min < max, "min must be less than max");
        return min <= value && value <= max;
    }

    public static boolean isBetween(final int value, final int min, final int max) {
        checkArgument(min < max, "min must be less than max");
        return min <= value && value <= max;
    }

    public static boolean isBetween(final long value, final long min, final long max) {
        checkArgument(min < max, "min must be less than max");
        return min <= value && value <= max;
    }
}
