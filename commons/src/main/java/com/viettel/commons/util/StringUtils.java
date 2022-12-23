package com.viettel.commons.util;

import java.util.List;

public class StringUtils {

    public static <T> String toString(List<T> lst) {
        if (lst == null) {
            return null;
        }
        var stringBuilder = new StringBuilder();
        lst.stream()
                .limit(lst.size() - 1)
                .forEach(value -> stringBuilder.append(value).append(','));
        stringBuilder.append(lst.get(lst.size() - 1));
        return stringBuilder.toString();
    }
}
