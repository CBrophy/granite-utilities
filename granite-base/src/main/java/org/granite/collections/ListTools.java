package org.granite.collections;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Random;
import org.granite.math.MathTools;

public class ListTools {

  public static <T> List<T> sublistPaging(final List<T> source,
      final int itemsPerPage,
      final int pageNum) {
    checkNotNull(source, "source");

    if (itemsPerPage > source.size() || itemsPerPage <= 0) {
      return source;
    }

    if (pageNum <= 1) {
      return firstPage(source, itemsPerPage);
    }

    int numPages = source.size() / itemsPerPage;

    if (numPages * itemsPerPage < source.size()) {
      numPages++;
    }

    if (pageNum >= numPages) {
      return lastPage(source, itemsPerPage);
    }

    int lastIndex = source.size() - 1;

    int startIndex = MathTools.minMaxBound((pageNum - 1) * itemsPerPage, 0, lastIndex);

    int endIndex = MathTools.minMaxBound(startIndex + itemsPerPage, 0, lastIndex);

    if (startIndex == 0 && endIndex == lastIndex) {
      return source;
    }

    return source.subList(startIndex, endIndex);

  }

  public static <T> List<T> sublistLimitOffset(final List<T> source,
      final int limit,
      final int offset) {
    checkNotNull(source, "source");

    if (limit <= 0 || offset < 0 || offset >= source.size()) {
      return ImmutableList.of();
    }

    // subList is toIndex is exclusive such that the last item
    // is retrieved with a non-existent index of array[maxIndex+1]
    int endIndex = offset + limit > source.size() ? source.size() : offset + limit;

    return source.subList(
        offset,
        endIndex
    );
  }

  private static <T> List<T> firstPage(
      final List<T> source,
      final int itemsPerPage) {

    checkNotNull(source, "source");

    if (itemsPerPage > source.size() || itemsPerPage <= 0) {
      return source;
    }

    return source.subList(0, itemsPerPage);
  }

  private static <T> List<T> lastPage(
      final List<T> source,
      final int itemsPerPage) {

    checkNotNull(source, "source");

    if (itemsPerPage > source.size() || itemsPerPage <= 0) {
      return source;
    }

    int lastIndex = source.size();

    final int numPages = lastIndex / itemsPerPage;

    final int remainder = lastIndex - (numPages * itemsPerPage);

    return source.subList(lastIndex - (remainder > 0 ? remainder : itemsPerPage), lastIndex);
  }

  public static <T> T random(
      final List<T> source
  ) {
    checkNotNull(source, "source");

    return source.isEmpty() ? null : source.get(new Random().nextInt(source.size()));
  }

  public static <T> boolean listsMatch(
      final ImmutableList<T> list1,
      final ImmutableList<T> list2) {
    checkNotNull(list1, "list1");
    checkNotNull(list2, "list2");

    if (list1.size() != list2.size()) {
      return false;
    }

    for (int index = 0; index < list1.size(); index++) {
      if (!list1.get(index).equals(list2.get(index))) {
        return false;
      }
    }

    return true;
  }
}
