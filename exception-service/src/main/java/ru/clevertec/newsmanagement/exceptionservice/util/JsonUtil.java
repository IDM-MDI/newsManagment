package ru.clevertec.newsmanagement.exceptionservice.util;

import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.clevertec.newsmanagement.exceptionservice.model.DTO;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

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
     * Converts a list of protobuf messages to a JSON string format.
     * @param messageOrBuilders the list of protobuf messages to convert
     * @return the messages converted to JSON string format
     * @throws IOException if there is an error printing the messages to JSON format
     */
    public static String toJson(List<? extends MessageOrBuilder> messageOrBuilders) {
        if(messageOrBuilders.isEmpty()) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        messageOrBuilders.forEach(message -> builder.append(toJson(message)).append(",\n"));
        builder.delete(builder.length() -2 ,builder.length());
        return "[\n" + builder + "\n]";
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