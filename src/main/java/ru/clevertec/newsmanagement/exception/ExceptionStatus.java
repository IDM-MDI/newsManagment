package ru.clevertec.newsmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum ExceptionStatus {
    USER_EXIST(1000,"User with current username is exist, try new one(or be original)"),
    USER_NOT_FOUND(1001,"User with current username not found"),
    ENTITY_NOT_FOUND(1002,"Lookup entity not found"),
    NO_ACCESS(1003,"No access to interact with entity"),
    EMPTY_PAGE(1004,"This page doesn't exist");
    private final int status;
    private final String message;
}
