package ru.clevertec.newsmanagement.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private final String exception;
    private final String url;
    private final LocalDateTime timestamp;
}

