package org.granite.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.Test;

public class ListToolsTest {

  private static List<Integer> createTestList() {

    final List<Integer> result = new ArrayList<>();

    for (int count = 1; count <= 20; count++) {
      result.add(count);
    }

    return result;
  }

  @Test
  public void testGetSublist() {

    final List<Integer> testList = createTestList();

    List<Integer> page4 = ListTools.sublistPaging(testList, 5, 4);
    List<Integer> page1 = ListTools.sublistPaging(testList, 5, 1);
    List<Integer> page2 = ListTools.sublistPaging(testList, 5, 2);
    List<Integer> page3 = ListTools.sublistPaging(testList, 5, 3);

    assertNotNull(page1);
    assertEquals(5, page1.size());
    assertTrue(checkResults(page1, 1, 2, 3, 4, 5));

    assertNotNull(page2);
    assertEquals(5, page2.size());
    assertTrue(checkResults(page2, 6, 7, 8, 9, 10));

    assertNotNull(page3);
    assertEquals(5, page3.size());
    assertTrue(checkResults(page3, 11, 12, 13, 14, 15));

    assertNotNull(page4);
    assertEquals(5, page4.size());
    assertTrue(checkResults(page4, 16, 17, 18, 19, 20));

    List<Integer> page5 = ListTools.sublistPaging(testList, 5, 5);

    assertNotNull(page5);
    assertEquals(5, page5.size());
    assertTrue(checkResults(page5, 16, 17, 18, 19, 20));

    List<Integer> page0 = ListTools.sublistPaging(testList, 5, 0);

    assertNotNull(page0);
    assertEquals(5, page0.size());
    assertTrue(checkResults(page0, 1, 2, 3, 4, 5));

    List<Integer> pageTooMuch = ListTools.sublistPaging(testList, 50, 1);

    assertNotNull(pageTooMuch);
    assertEquals(20, pageTooMuch.size());
    assertTrue(checkResults(pageTooMuch,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        18,
        19,
        20));

    List<Integer> pageTooLittle = ListTools.sublistPaging(testList, 0, 1);

    assertNotNull(pageTooLittle);
    assertEquals(20, pageTooLittle.size());
    assertTrue(checkResults(pageTooLittle,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        18,
        19,
        20));

    List<Integer> pageNotEnough = ListTools.sublistPaging(testList, 15, 2);

    assertNotNull(pageNotEnough);
    assertEquals(5, pageNotEnough.size());
    assertTrue(checkResults(pageNotEnough, 16, 17, 18, 19, 20));

    List<Integer> pageMiddleNotEnough = ListTools.sublistPaging(testList, 6, 3);

    assertNotNull(pageMiddleNotEnough);
    assertEquals(6, pageMiddleNotEnough.size());
    assertTrue(checkResults(pageMiddleNotEnough, 13, 14, 15, 16, 17, 18));
  }

  private boolean checkResults(List<Integer> results, int... keys) {

    final Set<Integer> testSet = results
        .stream()
        .collect(Collectors.toSet());

    for (int key : keys) {
      if (!testSet.contains(key)) {
        return false;
      }
    }

    return true;
  }

  @Test
  public void testSublistOffset() {
    final List<Integer> test = createTestList();

    final List<Integer> result = ListTools.sublistLimitOffset(
        test,
        4,
        3
    );

    assertEquals(4, result.size());
    assertEquals(4, (int) result.get(0));
    assertEquals(5, (int) result.get(1));
    assertEquals(6, (int) result.get(2));
    assertEquals(7, (int) result.get(3));

    final List<Integer> result2 = ListTools.sublistLimitOffset(test,
        1,
        0);

    assertEquals(1, result2.size());
    assertEquals(1, (int) result2.get(0));

    final List<Integer> result3 = ListTools.sublistLimitOffset(test,
        50,
        19);

    assertEquals(1, result3.size());
    assertEquals(20, (int) result3.get(0));

  }

  @Test
  public void testRandom() {
    final HashMap<Integer, AtomicInteger> results = new HashMap<>();

    final List<Integer> testList = createTestList();

    for (int count = 0; count < 1000; count++) {
      results
          .computeIfAbsent(ListTools.random(testList), key -> new AtomicInteger(0))
          .incrementAndGet();
    }

    assertEquals(testList.size(), results.size());

  }
}