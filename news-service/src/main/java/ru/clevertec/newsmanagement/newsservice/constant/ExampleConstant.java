package ru.clevertec.newsmanagement.newsservice.constant;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.ExampleMatcher;
/**
 * A utility class for handling Example matcher.
 * @author Dayanch
 */
@UtilityClass
public class ExampleConstant {
    public static ExampleMatcher ENTITY_SEARCH_MATCHER = ExampleMatcher
            .matchingAll()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
}
