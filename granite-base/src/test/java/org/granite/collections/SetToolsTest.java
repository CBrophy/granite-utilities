package org.granite.collections;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import org.junit.Test;

/**
 * Created by cbrophy on 8/2/17.
 */
public class SetToolsTest {

  @Test
  public void findDistinctCombinationSets() throws Exception {
    final HashMap<Integer, Set<Integer>> exclusions = new HashMap<>();
    exclusions.put(1, ImmutableSet.of(7, 8));
    exclusions.put(2, ImmutableSet.of(4, 5, 8));
    exclusions.put(3, ImmutableSet.of(4, 5, 9, 10, 11));
    exclusions.put(4, ImmutableSet.of(2, 3, 5));
    exclusions.put(5, ImmutableSet.of(2, 3, 4, 8));
    exclusions.put(7, ImmutableSet.of(1));
    exclusions.put(8, ImmutableSet.of(1, 2, 5));
    exclusions.put(9, ImmutableSet.of(3));
    exclusions.put(10, ImmutableSet.of(3));
    exclusions.put(11, ImmutableSet.of(3));

    final List<SortedSet<Integer>> distinctSets = SetTools.findDistinctCombinationSets(
        ImmutableSet.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
        exclusions
    );

    final HashSet<SortedSet<Integer>> answers = new HashSet<>();
    final ImmutableSortedSet<Integer> ans1 = ImmutableSortedSet.of(4, 6, 7, 8, 9, 10, 11, 12);
    final ImmutableSortedSet<Integer> ans2 = ImmutableSortedSet.of(3, 6, 7, 8, 12);
    final ImmutableSortedSet<Integer> ans3 = ImmutableSortedSet.of(1, 5, 6, 9, 10, 11, 12);
    final ImmutableSortedSet<Integer> ans4 = ImmutableSortedSet.of(2, 6, 7, 9, 10, 11, 12);
    final ImmutableSortedSet<Integer> ans5 = ImmutableSortedSet.of(5, 6, 7, 9, 10, 11, 12);

    answers.add(ans1);
    answers.add(ans2);
    answers.add(ans3);
    answers.add(ans4);
    answers.add(ans5);

    assertEquals(5, distinctSets.size());

    for (SortedSet<Integer> distinctSet : distinctSets) {
      assertTrue(answers.contains(distinctSet));
    }

  }

  @Test
  public void groupSimilarExclusions() throws Exception {
    final HashMap<Integer, Set<Integer>> test = new HashMap<>();
    test.put(1, ImmutableSet.of(7, 8));
    test.put(2, ImmutableSet.of(4, 5, 8));
    test.put(3, ImmutableSet.of(9, 10, 11));
    test.put(4, ImmutableSet.of(3, 5));
    test.put(5, ImmutableSet.of(2, 4, 8));

    List<Set<Integer>> result = SetTools.groupBySharedExclusions(test);

    assertEquals(2, result.size());

    for (Set<Integer> integers : result) {
      assertTrue(integers.size() == 1 || integers.size() == 4);

      if (integers.size() == 1) {
        assertTrue(integers.contains(3));
      } else {
        assertTrue(integers.contains(1));
        assertTrue(integers.contains(2));
        assertTrue(integers.contains(4));
        assertTrue(integers.contains(5));
      }
    }

  }

}