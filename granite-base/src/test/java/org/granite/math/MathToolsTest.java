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

import org.junit.Test;

import static org.junit.Assert.*;

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

}