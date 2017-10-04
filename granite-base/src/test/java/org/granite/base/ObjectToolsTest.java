package org.granite.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * User: cbrophy Date: 10/4/17 Time: 2:29 PM
 */
public class ObjectToolsTest {

  @Test
  public void testFirstNonNull() {
    final Integer result1 = ObjectTools
        .firstNonNull(null, 90, 80);

    assertNotNull(result1);
    assertEquals(90, (int) result1);
  }
}