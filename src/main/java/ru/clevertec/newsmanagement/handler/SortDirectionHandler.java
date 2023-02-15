package ru.clevertec.newsmanagement.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortDirectionHandler {
    private static final String DESC = "desc";
    public static Sort getDirection(Sort sort, String direction) {
        return DESC.equalsIgnoreCase(direction) ? sort.descending() : sort.ascending();
    }
}
