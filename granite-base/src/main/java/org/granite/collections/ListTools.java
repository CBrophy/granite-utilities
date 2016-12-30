package org.granite.collections;

import com.google.common.collect.ImmutableList;

import org.granite.math.MathTools;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListTools {

    public static <T> List<T> sublistPaging(final List<T> source,
                                            final int itemsPerPage,
                                            final int pageNum) {
        checkNotNull(source, "source");

        if (itemsPerPage > source.size() || itemsPerPage <= 0) return source;

        if (pageNum <= 1) return firstPage(source, itemsPerPage);

        int numPages = source.size() / itemsPerPage;

        if (numPages * itemsPerPage < source.size()) numPages++;

        if (pageNum >= numPages) return lastPage(source, itemsPerPage);

        int lastIndex = source.size() - 1;

        int startIndex = MathTools.minMaxBound((pageNum - 1) * itemsPerPage, 0, lastIndex);

        int endIndex = MathTools.minMaxBound(startIndex + itemsPerPage, 0, lastIndex);

        if (startIndex == 0 && endIndex == lastIndex) return source;

        return source.subList(startIndex, endIndex);

    }

    public static <T> List<T> sublistLimitOffset(final List<T> source,
                                                 final int limit,
                                                 final int offset) {
        checkNotNull(source, "source");

        if (limit <= 0 || offset < 0 || offset >= source.size()) return ImmutableList.of();

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

        if (itemsPerPage > source.size() || itemsPerPage <= 0) return source;

        return source.subList(0, itemsPerPage);
    }

    private static <T> List<T> lastPage(
            final List<T> source,
            final int itemsPerPage) {

        checkNotNull(source, "source");

        if (itemsPerPage > source.size() || itemsPerPage <= 0) return source;

        int lastIndex = source.size();

        final int numPages = lastIndex / itemsPerPage;

        final int remainder = lastIndex - (numPages * itemsPerPage);

        return source.subList(lastIndex - (remainder > 0 ? remainder : itemsPerPage), lastIndex);
    }
}
