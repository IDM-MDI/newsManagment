package ru.clevertec.newsmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Exception response")
public class ExceptionResponse {
    @Schema(description = "Information about exception")
    private final String exception;
    @Schema(description = "The URL to which the request was sent")
    private final String url;
    @Schema(description = "The time when the exception occurred")
    private final LocalDateTime timestamp;
}

