package ru.clevertec.newsmanagement.exceptionservice.exception;

import lombok.AllArgsConstructor;
import lombok.ToString;


/**
 * This enum defines the exception status codes and messages that can be used in the application.
 * @author Dayanch
 */
@AllArgsConstructor
@ToString
public enum ExceptionStatus {
    USER_EXIST(1000,"User with current username is exist, try new one(or be original)"),
    USER_NOT_FOUND(1001,"User with current username not found"),
    ENTITY_NOT_FOUND(1002,"Lookup entity not found"),
    NO_ACCESS(1003,"No access to interact with entity"),
    EMPTY_PAGE(1004,"This page doesn't exist"),
    USER_NOT_AUTHORIZE(1005,"User not authorize, please login to use api"),
    JWT_NOT_VALID(1006, "Current JWT not valid");
    private final int status;
    private final String message;
}
