package ru.clevertec.newsmanagement.exceptionservice.exception;


/**
 * CustomException class extends Exception class, and it represents a custom exception in the application.
 * @author Dayanch
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
