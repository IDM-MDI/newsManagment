package ru.clevertec.newsmanagement.newsservice.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.model.UserDTO;

import java.util.Map;
import java.util.Objects;


/**
 * Utility class for parsing query parameters and converting them into DTO objects.
 * Provides methods for parsing query parameters and converting them into {@link DTO.Comment} and {@link DTO.News}.
 * @author Dayanch
 */
@UtilityClass
public class QueryParameterUtil {
    private static final String USERNAME = "username";
    private static final String TEXT = "text";
    private static final String TITLE = "title";

    /**
     * Parses the given query parameters and returns a {@link DTO.Comment} object.
     * If the query parameter is not found, an empty string will be used.
     * @param query the map of query parameters
     * @return a {@link DTO.Comment} object containing the parsed query parameters
     */
    public static DTO.Comment getCommentByQuery(@NotNull Map<String,String[]> query) {
        return DTO.Comment.newBuilder()
                .setUsername(parseToString(query,USERNAME))
                .setText(parseToString(query,TEXT))
                .build();
    }

    /**
     * Parses the given query parameters and returns a {@link DTO.News} object.
     * If the query parameter is not found, an empty string will be used.
     * @param query the map of query parameters
     * @return a {@link DTO.News} object containing the parsed query parameters
     */
    public static DTO.News getNewsByQuery(@NotNull Map<String, String[]> query) {
        return DTO.News.newBuilder()
                .setUsername(parseToString(query,USERNAME))
                .setTitle(parseToString(query,TITLE))
                .setText(parseToString(query,TEXT))
                .build();
    }
    /**
     * Returns a UserDTO object constructed from the headers of the given HttpServletRequest object.
     * The returned UserDTO object has its username, role, and jwt fields set to the values of the
     * "username", "role", and "auth-token" headers of the HttpServletRequest object, respectively.
     * @param request the HttpServletRequest object to extract user information from
     * @return a UserDTO object constructed from the headers of the given HttpServletRequest object
     */
    public static UserDTO getUser(HttpServletRequest request) {
        return UserDTO.builder()
                .username(request.getHeader("username"))
                .role(request.getHeader("role"))
                .jwt(request.getHeader("auth-token"))
                .build();
    }

    /**
     * Parses the given query parameter and returns a string.
     * If the query parameter is not found, an empty string will be used.
     * @param query the map of query parameters
     * @param queryName the name of the query parameter
     * @return the parsed string value of the query parameter
     */
    private static String parseToString(Map<String,String[]> query, String queryName) {
        String[] strings = query.get(queryName);
        return (Objects.isNull(strings) || strings.length == 0) ? "" : strings[0];
    }
}