package ru.clevertec.newsmanagement.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;
/**
 * A utility class for handling Example matcher.
 * @author Dayanch
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleUtil {
    public static ExampleMatcher ENTITY_SEARCH_MATCHER = ExampleMatcher
            .matchingAll()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
}
