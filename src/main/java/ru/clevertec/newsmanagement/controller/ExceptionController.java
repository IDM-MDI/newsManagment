package ru.clevertec.newsmanagement.controller;

import com.google.protobuf.Timestamp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;

import java.sql.SQLException;
import java.time.Instant;

import static ru.clevertec.newsmanagement.util.DtoUtil.toJson;


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
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException exception, HttpServletRequest request) {
        Instant now = Instant.now();
        return toJson(
                DTO.Exception.newBuilder()
                        .setException(exception.getMessage())
                        .setUrl(request.getRequestURL().toString())
                        .setCreatedDate(
                                Timestamp.newBuilder()
                                        .setSeconds(now.getEpochSecond())
                                        .setNanos(now.getNano())
                                        .build()
                        )
                        .build()
        );
    }


    /**
     * Handles MethodArgumentNotValidException and HttpMessageNotReadableException and returns a validation exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the validation exception response
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public String handleMethodArgumentException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Instant now = Instant.now();
        return toJson(
                DTO.Exception.newBuilder()
                        .setException(
                                exception.getBindingResult()
                                .getFieldError()
                                .getDefaultMessage()
                        )
                        .setUrl(
                                request.getRequestURL()
                                        .toString()
                        )
                        .setCreatedDate(
                                Timestamp.newBuilder()
                                        .setSeconds(now.getEpochSecond())
                                        .setNanos(now.getNano())
                                        .build()
                        )
                        .build()
        );
    }


    /**
     * Handles UsernameNotFoundException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException exception, HttpServletRequest request) {
        Instant now = Instant.now();
        return toJson(
                DTO.Exception.newBuilder()
                        .setException(exception.getMessage())
                        .setUrl(request.getRequestURL().toString())
                        .setCreatedDate(
                                Timestamp.newBuilder()
                                        .setSeconds(now.getEpochSecond())
                                        .setNanos(now.getNano())
                                        .build()
                        )
                        .build()
        );
    }


    /**
     * Handles HttpRequestMethodNotSupportedException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleMethodNotSupportException(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        Instant now = Instant.now();
        return toJson(
                DTO.Exception.newBuilder()
                        .setException(exception.getMessage())
                        .setUrl(request.getRequestURL().toString())
                        .setCreatedDate(
                                Timestamp.newBuilder()
                                        .setSeconds(now.getEpochSecond())
                                        .setNanos(now.getNano())
                                        .build()
                        )
                        .build()
        );
    }


    /**
     * Handles NoSuchMethodException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchMethodException.class)
    public String handleNotFoundException(NoSuchMethodException exception, HttpServletRequest request) {
        Instant now = Instant.now();
        return toJson(
                DTO.Exception.newBuilder()
                        .setException(exception.getMessage())
                        .setUrl(request.getRequestURL().toString())
                        .setCreatedDate(
                                Timestamp.newBuilder()
                                        .setSeconds(now.getEpochSecond())
                                        .setNanos(now.getNano())
                                        .build()
                        )
                        .build()
        );
    }


    /**
     * Handles ConstraintViolationException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintException(ConstraintViolationException exception, HttpServletRequest request) {
        Instant now = Instant.now();
        return toJson(
                DTO.Exception.newBuilder()
                        .setException(exception.getMessage())
                        .setUrl(request.getRequestURL().toString())
                        .setCreatedDate(
                                Timestamp.newBuilder()
                                        .setSeconds(now.getEpochSecond())
                                        .setNanos(now.getNano())
                                        .build()
                        )
                        .build()
        );
    }
    /**
     * Handles SQLException and DataAccessException and returns a custom exception response with the corresponding HTTP status code.
     * @param exception the exception to be handled
     * @param request the HTTP request
     * @return the custom exception response
     */
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String handleSQLException(SQLException exception, HttpServletRequest request) {
        Instant now = Instant.now();
        return toJson(
                DTO.Exception.newBuilder()
                        .setException(exception.getMessage())
                        .setUrl(request.getRequestURL().toString())
                        .setCreatedDate(
                                Timestamp.newBuilder()
                                        .setSeconds(now.getEpochSecond())
                                        .setNanos(now.getNano())
                                        .build()
                        )
                        .build()
        );
    }
}
