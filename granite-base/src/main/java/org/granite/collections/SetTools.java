package org.granite.collections;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

public class SetTools {

  public static <T> List<SortedSet<T>> findDistinctCombinationSets(
      final Set<T> universalSet,
      final Map<T, Set<T>> exclusions) {
    checkNotNull(universalSet, "universalSet");
    checkNotNull(exclusions, "exclusions");

    if (exclusions.isEmpty()) {
      return ImmutableList.of(ImmutableSortedSet.copyOf(universalSet));
    }

    final ImmutableList.Builder<SortedSet<T>> result = ImmutableList.builder();

    final HashSet<HashSet<T>> uniqueExclusionSets = new HashSet<>();

    for (Entry<T, Set<T>> exclusionEntry : exclusions.entrySet()) {
      final HashSet<T> initial = new HashSet<>(universalSet);

      initial.removeAll(exclusionEntry.getValue());

      for (Entry<T, Set<T>> otherExclusionEntry : exclusions.entrySet()) {
        if(exclusionEntry.getKey().equals(otherExclusionEntry.getKey())) continue;

        if(Sets.intersection(otherExclusionEntry.getValue(), initial).size() > 0){
          initial.remove(otherExclusionEntry.getKey());
        }
      }

      uniqueExclusionSets.add(initial);
    }

    for (HashSet<T> uniqueExclusionSet : uniqueExclusionSets) {
      boolean isSubset = false;
      for (HashSet<T> otherExclusionSet : uniqueExclusionSets) {
        if (otherExclusionSet.size() > uniqueExclusionSet.size()
            && Sets.difference(uniqueExclusionSet, otherExclusionSet).size() == 0) {
          isSubset = true;
          break;
        }
      }

      if (!isSubset) {
        result.add(ImmutableSortedSet.copyOf(uniqueExclusionSet));
      }
    }

    return result.build();
  }

  public static <T> List<Set<T>> groupBySharedExclusions(final Map<T, Set<T>> exclusions) {
    final HashMap<T, Set<T>> reverseExclusionMap = new HashMap<>();
    final HashMap<T, Set<T>> groupingMap = new HashMap<>();

    // Map items together by what they exclude
    for (Entry<T, Set<T>> exclusionEntry : exclusions.entrySet()) {
      for (T excludedBy : exclusionEntry.getValue()) {
        reverseExclusionMap
            .computeIfAbsent(excludedBy, key -> new HashSet<>())
            .add(exclusionEntry.getKey());
      }
    }

    // Create sets of items that share any exclusions
    for (Set<T> similarItems : reverseExclusionMap.values()) {
      final Set<T> groupSet = new HashSet<>();

      for (T item : similarItems) {
        groupSet.add(item);

        final Set<T> currentGroupSet = groupingMap.get(item);

        if (currentGroupSet == null) {
          groupingMap.put(item, groupSet);
        } else {
          groupSet.addAll(currentGroupSet);

          for (T associatedItem : currentGroupSet) {
            groupingMap.put(associatedItem, groupSet);
          }
        }
      }
    }

    final Set<Set<T>> uniqueSets = new HashSet<>();

    uniqueSets.addAll(groupingMap
        .values());

    return ImmutableList.copyOf(uniqueSets);
  }
}
