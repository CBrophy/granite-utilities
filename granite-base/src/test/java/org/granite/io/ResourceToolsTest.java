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

package org.granite.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import org.junit.Test;

public class ResourceToolsTest {

  @Test
  public void testReadResourceTextFile() throws Exception {

    final List<String> resource1Values = ResourceTools.readResourceTextFile("test-resource.txt");
    final List<String> resource2Values = ResourceTools
        .readResourceTextFile("test-resource2.txt.gz");

    assertNotNull(resource1Values);
    assertNotNull(resource2Values);

    assertEquals(5, resource1Values.size());
    assertEquals(5, resource2Values.size());

    assertEquals("This", resource1Values.get(0));
    assertEquals("is", resource1Values.get(1));
    assertEquals("a", resource1Values.get(2));
    assertEquals("test", resource1Values.get(3));
    assertEquals("!", resource1Values.get(4));

    assertEquals("This", resource2Values.get(0));
    assertEquals("is", resource2Values.get(1));
    assertEquals("a", resource2Values.get(2));
    assertEquals("test", resource2Values.get(3));
    assertEquals("!", resource2Values.get(4));

  }
}