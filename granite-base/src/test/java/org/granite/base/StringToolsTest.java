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

package org.granite.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import java.util.Map;
import org.junit.Test;

public class StringToolsTest {

  @Test
  public void testIsNullOrEmpty() throws Exception {
    String test1 = null;
    String test2 = "      ";
    String test3 = "   askjdh";
    String test4 = "test     ";

    assertTrue(StringTools.isNullOrEmpty(test1));
    assertTrue(StringTools.isNullOrEmpty(test2));
    assertFalse(StringTools.isNullOrEmpty(test3));
    assertFalse(StringTools.isNullOrEmpty(test4));
  }

  @Test
  public void testAnyNullOrEmpty() {
    assertTrue(StringTools.anyNullOrEmpty("test", "", "test2", "   "));
    assertTrue(StringTools.anyNullOrEmpty("test1", null, "tet"));
    assertFalse(StringTools.anyNullOrEmpty("test1", "Test2", "test3"));
  }

  @Test
  public void testTruncate() {
    final String test1 = "hello";

    assertEquals("h", StringTools.truncate(test1, 1));
    assertEquals("hell", StringTools.truncate(test1, 4));
    assertEquals("hello", StringTools.truncate(test1, 900));
  }

  @Test
  public void testStringToMap() {
    final String test1 = "k1=v1,k2=v2,k2=,k3=v3,k3=v4,k4=";

    final Map<String, String> resultNoEmpty = StringTools.convertStringsToMap(
        test1,
        CharMatcher.is(','),
        CharMatcher.is('='),
        true
    );

    assertEquals(3, resultNoEmpty.size());

    final Map<String, String> resultEmpty = StringTools.convertStringsToMap(
        test1,
        CharMatcher.is(','),
        CharMatcher.is('='),
        false
    );

    assertEquals(4, resultEmpty.size());

    assertEquals("v1", resultNoEmpty.get("k1"));
    assertEquals("v2", resultNoEmpty.get("k2"));
    assertEquals("v4", resultNoEmpty.get("k3"));

    assertEquals("v1", resultEmpty.get("k1"));
    assertEquals("", resultEmpty.get("k2"));
    assertEquals("v4", resultEmpty.get("k3"));
    assertEquals("", resultEmpty.get("k4"));


  }
}