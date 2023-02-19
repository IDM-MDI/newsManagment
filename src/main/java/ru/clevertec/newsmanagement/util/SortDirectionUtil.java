package ru.clevertec.newsmanagement.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortDirectionUtil {
    private static final String DESC = "desc";
    public static Sort getDirection(Sort sort, String direction) {
        return DESC.equalsIgnoreCase(direction) ? sort.descending() : sort.ascending();
    }
}
