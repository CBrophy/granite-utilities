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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

public class ProbabilityTools {

  /**
   * Determines the probability union of a list of independent probabilities
   *
   * P(A u B u C) = P(A) + P(B) + P(C) - P(AB) - P(AC) - P(BC) + P(ABC)
   *
   * @param probabilities Independent probability values to union
   * @return The corrected union of the probabilities, 0.0 when probabilities is empty, and
   * probabilities[0] when there is only one value
   */
  public static double independentUnion(final List<Double> probabilities) {
    checkNotNull(probabilities, "probabilities");

    if (probabilities.isEmpty()) {
      return 0.0;
    }

    if (probabilities.size() == 1) {
      return probabilities.get(0);
    }

    double result = 0.0;
    double correction = 0.0;
    double all = 1.0;

    for (int index = 0; index < probabilities.size(); index++) {

      final double probability = probabilities.get(index);

      result += probability;

      all *= probability;

      for (int nextIndex = index + 1; nextIndex < probabilities.size(); nextIndex++) {
        final double nextProbability = probabilities.get(nextIndex);

        correction += (probability * nextProbability);
      }
    }

    return result - correction + all;
  }
}
