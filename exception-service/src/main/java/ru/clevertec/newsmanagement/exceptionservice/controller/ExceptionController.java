package ru.clevertec.newsmanagement.exceptionservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.clevertec.newsmanagement.exceptionservice.exception.CustomException;

import java.sql.SQLException;

import static ru.clevertec.newsmanagement.exceptionservice.util.JsonUtil.getJSONStringException;

/**
 * This class handles exceptions thrown by the application controllers and provides a unified error response.
 * @author Dayanch
 */
@RestControllerAdvice
public class ExceptionController {

    /**
     * Handles CustomException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJSONStringException(request,exception.getMessage()));
    }


    /**
     * Handles MethodArgumentNotValidException and HttpMessageNotReadableException and returns a validation exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the validation exception response
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleMethodArgumentException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJSONStringException(
                                request,
                                exception.getBindingResult()
                                        .getFieldError()
                                        .getDefaultMessage()
                        )
                );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJSONStringException(request, exception.getMessage()));
    }


    /**
     * Handles HttpRequestMethodNotSupportedException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupportException(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJSONStringException(request,exception.getMessage()));
    }


    /**
     * Handles NoSuchMethodException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseEntity<String> handleNotFoundException(NoSuchMethodException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJSONStringException(request, exception.getMessage()));
    }
    /**
     * Handles ConstraintViolationException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintException(ConstraintViolationException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJSONStringException(request,exception.getMessage()));
    }
    /**
     * Handles SQLException and DataAccessException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<String> handleSQLException(SQLException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getJSONStringException(request,exception.getMessage()));
    }
}
