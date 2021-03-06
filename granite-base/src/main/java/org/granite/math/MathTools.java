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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.math.DoubleMath;
import java.io.Serializable;
import java.math.RoundingMode;

public final class MathTools implements Serializable {

  private MathTools() {
  }

  public static double exponentialDecay(final double value, final int time,
      final double decayCoefficient) {
    return value * Math.exp(time * decayCoefficient);
  }

  public static double exponentialDecay(final double value, final long time,
      final double decayCoefficient) {
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

  public static int minMaxBound(final int value, final int min, final int max) {
    checkArgument(min < max, "min must be less than max");
    return Math.max(Math.min(value, max), min);
  }

  public static long minMaxBound(final long value, final long min, final long max) {
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

  public static int mod(final int numerator, final int denominator) {
    return (numerator % denominator + denominator) % denominator;
  }

  public static long mod(final long numerator, final long denominator) {
    return (numerator % denominator + denominator) % denominator;
  }

  public static int min(int... values) {
    checkNotNull(values, "values");

    int min = Integer.MAX_VALUE;

    for (int value : values) {
      min = Math.min(min, value);
    }

    return min;
  }

  public static long min(long... values) {
    checkNotNull(values, "values");

    long min = Long.MAX_VALUE;

    for (long value : values) {
      min = Math.min(min, value);
    }

    return min;
  }

  public static double min(double... values) {
    checkNotNull(values, "values");

    double min = Double.MAX_VALUE;

    for (double value : values) {
      min = Math.min(min, value);
    }

    return min;
  }

  public static int max(int... values) {
    checkNotNull(values, "values");

    int max = Integer.MIN_VALUE;

    for (int value : values) {
      max = Math.max(max, value);
    }

    return max;
  }

  public static long max(long... values) {
    checkNotNull(values, "values");

    long max = Long.MIN_VALUE;

    for (long value : values) {
      max = Math.max(max, value);
    }

    return max;
  }

  public static double max(double... values) {
    checkNotNull(values, "values");

    double max = Double.MIN_VALUE * -1.0;

    for (double value : values) {
      max = Math.max(max, value);
    }

    return max;
  }

}
