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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MathToolsTest {

  @Test
  public void testDecay() {
    double decayedValue = MathTools.exponentialDecay(1.0, 28, -0.024);

    assertEquals(0.5, decayedValue, 0.011);

    double nonDecayedValue = MathTools.exponentialDecay(6.0, 28, 0.0);

    assertEquals(6.0, nonDecayedValue, 0.011);

  }

  @Test
  public void testRounding() {
    double r1 = MathTools.round(0.9466726, 2);

    assertEquals(0.95, r1, 0.0001);

    double r2 = MathTools.round(0.9436726, 2);

    assertEquals(0.94, r2, 0.0001);
  }

  @Test
  public void testMinMaxBound() {
    assertEquals(0.0, MathTools.minMaxBound(-1.0, 0.0, 1.0), 0.0);
    assertEquals(1.0, MathTools.minMaxBound(2.0, 0.0, 1.0), 0.0);

    assertEquals(0L, MathTools.minMaxBound(-1L, 0L, 1L));
    assertEquals(1L, MathTools.minMaxBound(2L, 0L, 1L));

    assertEquals(0, MathTools.minMaxBound(-1, 0, 1));
    assertEquals(1, MathTools.minMaxBound(2, 0, 1), 0);
  }

  @Test
  public void testIsBetween() {
    assertTrue(MathTools.isBetween(0.1, 0.0, 1.0));
    assertFalse(MathTools.isBetween(0.0, 0.1, 1.0));
  }

  @Test
  public void testModulus() {
    assertEquals(MathTools.mod(0, 7), MathTools.mod(-7, 7));
    assertEquals(MathTools.mod(1, 7), MathTools.mod(-6, 7));
    assertEquals(MathTools.mod(2, 7), MathTools.mod(-5, 7));
    assertEquals(MathTools.mod(3, 7), MathTools.mod(-4, 7));
    assertEquals(MathTools.mod(4, 7), MathTools.mod(-3, 7));
    assertEquals(MathTools.mod(5, 7), MathTools.mod(-2, 7));
    assertEquals(MathTools.mod(6, 7), MathTools.mod(-1, 7));
  }

  @Test
  public void testMin() {
    assertEquals(-15, MathTools.min(-6, 99, 100, -15, 0, 267));
    assertEquals(-15L, MathTools.min(-6L, 99L, 100L, -15L, 267L));
    assertEquals(-15.0, MathTools.min(-6.0, 99.0, 100.0, -15.0, 267.0), 0.00000001);
  }

  @Test
  public void testMax() {
    assertEquals(267, MathTools.max(-6, 99, 100, -15, 0, 267));
    assertEquals(267L, MathTools.max(-6L, 99L, 100L, -15L, 267L));
    assertEquals(267.0, MathTools.max(-6.0, 99.0, 100.0, -15.0, 267.0), 0.00000001);
  }
}