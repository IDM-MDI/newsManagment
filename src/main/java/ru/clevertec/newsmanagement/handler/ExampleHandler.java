package ru.clevertec.newsmanagement.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleHandler {
    public static ExampleMatcher ENTITY_SEARCH_MATCHER = ExampleMatcher
            .matchingAll()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
}
