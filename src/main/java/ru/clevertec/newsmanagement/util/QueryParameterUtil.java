package ru.clevertec.newsmanagement.util;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.clevertec.newsmanagement.model.DTO;

import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryParameterUtil {
    private static final String USERNAME = "username";
    private static final String TEXT = "text";
    private static final String TITLE = "title";
    public static DTO.Comment getCommentByQuery(@NotNull Map<String,String[]> query) {
        return DTO.Comment.newBuilder()
                .setUsername(parseToString(query,USERNAME))
                .setText(parseToString(query,TEXT))
                .build();
    }
    public static DTO.News getNewsByQuery(@NotNull Map<String, String[]> query) {
        return DTO.News.newBuilder()
                .setUsername(parseToString(query,USERNAME))
                .setTitle(parseToString(query,TITLE))
                .setText(parseToString(query,TEXT))
                .build();
    }
    private static String parseToString(Map<String,String[]> query, String queryName) {
        String[] strings = query.get(queryName);
        return (Objects.isNull(strings) || strings.length == 0) ? "" : strings[0];
    }
}
