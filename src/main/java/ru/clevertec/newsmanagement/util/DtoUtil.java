package ru.clevertec.newsmanagement.util;

import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.clevertec.newsmanagement.exception.CustomException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.EMPTY_PAGE;

/**
 * Utility class for DTO related operations.
 * @author Dayanch
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoUtil {

    /**
     * Checks if a given list is not null and not empty.
     * @param page the list to check
     * @throws CustomException if the list is null or empty
     */
    public static void checkPageListExist(List<?> page) throws CustomException {
        if (Objects.isNull(page) || page.isEmpty()) {
            throw new CustomException(EMPTY_PAGE.toString());
        }
    }


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
        StringBuilder builder = new StringBuilder();
        messageOrBuilders.forEach(message -> builder.append(toJson(message)));
        return "[" + builder + "]";
    }
}