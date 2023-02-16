package ru.clevertec.newsmanagement.controller;

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
import ru.clevertec.newsmanagement.model.ExceptionResponse;

import java.sql.SQLException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(CustomException.class)
    public ExceptionResponse handleCustomException(CustomException exception, HttpServletRequest request) {
        return ExceptionResponse.builder()
                .exception(exception.getMessage())
                .url(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ExceptionResponse handleMethodArgumentException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return ExceptionResponse.builder()
                .exception(
                        exception.getBindingResult()
                                .getFieldError()
                                .getDefaultMessage()
                )
                .url(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ExceptionResponse handleUsernameNotFoundException(UsernameNotFoundException exception, HttpServletRequest request) {
        return ExceptionResponse.builder()
                .exception(exception.getMessage())
                .url(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ExceptionResponse handleMethodNotSupportException(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        return ExceptionResponse.builder()
                .exception(exception.getMessage())
                .url(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchMethodException.class)
    public ExceptionResponse handleNotFoundException(NoSuchMethodException exception, HttpServletRequest request) {
        return ExceptionResponse.builder()
                .exception(exception.getMessage())
                .url(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ExceptionResponse handleConstraintException(ConstraintViolationException exception, HttpServletRequest request) {
        return ExceptionResponse.builder()
                .exception(exception.getMessage())
                .url(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ExceptionResponse handleSQLException(SQLException exception, HttpServletRequest request) {
        return ExceptionResponse.builder()
                .exception(exception.getMessage())
                .url(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
