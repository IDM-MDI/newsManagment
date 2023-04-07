package ru.clevertec.newsmanagement.userservice.util;

import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.clevertec.newsmanagement.userservice.model.DTO;

import java.io.IOException;
import java.time.Instant;


/**
 * Utility class for DTO related operations.
 * @author Dayanch
 */
@UtilityClass
public class JsonUtil {

    /**
     * Converts a protobuf message to JSON string format.
     * @param messageOrBuilder the protobuf message to convert
     * @return the message converted to JSON string format
     * @throws IOException if there is an error printing the message to JSON format
     */
    @SneakyThrows
    public static String toJson(MessageOrBuilder messageOrBuilder) {
        return JsonFormat.printer().print(messageOrBuilder);
    }

    /**
     * Generates a JSON string representation of an exception in the format of a
     * {@link DTO.Exception} protobuf message.
     * @param request the HTTP request that triggered the exception
     * @param message the error message to include in the exception
     * @return a JSON string representation of the exception as a {@link DTO.Exception} object
     * @throws NullPointerException if the {@code request} parameter is null
     * @throws IllegalArgumentException if the {@code message} parameter is blank
     */
    public static String getJSONStringException(@NotNull HttpServletRequest request, @NotBlank String message) {
        Instant now = Instant.now();
        return toJson(
                DTO.Exception.newBuilder()
                        .setException(message)
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