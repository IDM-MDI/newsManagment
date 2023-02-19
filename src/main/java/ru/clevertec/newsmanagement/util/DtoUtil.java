package ru.clevertec.newsmanagement.util;

import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.clevertec.newsmanagement.exception.CustomException;

import java.util.List;
import java.util.Objects;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.EMPTY_PAGE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoUtil {
    public static void checkPageListExist(List<?> page) throws CustomException {
        if(Objects.isNull(page) || page.isEmpty()) {
            throw new CustomException(EMPTY_PAGE.toString());
        }
    }
    @SneakyThrows
    public static String toJson(MessageOrBuilder messageOrBuilder) {
        return JsonFormat.printer().print(messageOrBuilder);
    }
    public static String toJson(List<? extends MessageOrBuilder> messageOrBuilders) {
        StringBuilder builder = new StringBuilder();
        messageOrBuilders.forEach(message -> builder.append(toJson(message)));
        return "[" + builder + "]";
    }
 }
