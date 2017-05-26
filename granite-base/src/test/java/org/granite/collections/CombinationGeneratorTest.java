package org.granite.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 * User: cbrophy
 * Date: 5/26/17
 * Time: 9:49 AM
 */
public class CombinationGeneratorTest {

  @Test
  public void testComboGenerator() {
    final String[] elements = {"a", "b", "c", "d"};
    int[] indices;
    final CombinationGenerator x = new CombinationGenerator(elements.length, 2);
    StringBuilder currentCombo = null;
    final Set<String> results = new HashSet<>();
    final Set<String> expected = ImmutableSet
        .of("ab","ac","ad","bc","bd", "cd");

    while (x.hasMore()) {
      currentCombo = new StringBuilder();

      indices = x.getNext();

      for (int i = 0; i < indices.length; i++) {
        currentCombo = currentCombo.append(elements[indices[i]]);
      }
      results.add(currentCombo.toString());
    }

    assertEquals(expected.size(), results.size());

    final SetView<String> diff = Sets.difference(results, expected);

    assertTrue(diff.isEmpty());
  }
}