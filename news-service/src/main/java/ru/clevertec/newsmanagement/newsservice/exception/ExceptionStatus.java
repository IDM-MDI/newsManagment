package ru.clevertec.newsmanagement.newsservice.exception;

import lombok.AllArgsConstructor;
import lombok.ToString;


/**
 * This enum defines the exception status codes and messages that can be used in the application.
 * @author Dayanch
 */
@AllArgsConstructor
@ToString
public enum ExceptionStatus {
    ENTITY_NOT_FOUND(1002,"Lookup entity not found"),
    NO_ACCESS(1003,"No access to interact with entity"),
    EMPTY_PAGE(1004,"This page doesn't exist");
    private final int status;
    private final String message;
}
