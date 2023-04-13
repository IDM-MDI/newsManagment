package ru.clevertec.newsmanagement.newsservice.util;


import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.model.UserDTO;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.CommentBuilder.aComment;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.NewsBuilder.aNews;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.UserBuilder.aUser;

@ExtendWith(MockitoExtension.class)
class QueryParameterUtilTest {
    @Mock
    private HttpServletRequest request;

    @Test
    void getCommentByQueryShouldReturnComment() {
        DTO.Comment comment = aComment().buildToDTO();
        DTO.Comment expected = DTO.Comment.newBuilder()
                .setUsername(comment.getUsername())
                .setText(comment.getText())
                .build();

        Map<String, String[]> query = new HashMap<>();
        query.put("username", new String[]{comment.getUsername()});
        query.put("text", new String[]{comment.getText()});

        DTO.Comment actual = QueryParameterUtil.getCommentByQuery(query);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getNewsByQueryShouldReturnNews() {
        DTO.News news = aNews().buildToDTO();
        DTO.News expected = DTO.News.newBuilder()
                .setUsername(news.getUsername())
                .setTitle(news.getTitle())
                .setText(news.getText())
                .build();

        Map<String, String[]> query = new HashMap<>();
        query.put("username", new String[]{news.getUsername()});
        query.put("title", new String[]{news.getTitle()});
        query.put("text", new String[]{news.getText()});

        DTO.News actual = QueryParameterUtil.getNewsByQuery(query);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void getUserShouldReturnUser() {
        UserDTO expected = aUser().buildToDTO();
        doReturn(expected.getUsername())
                .when(request).getHeader("username");
        doReturn(expected.getRole())
                .when(request).getHeader("role");
        doReturn(expected.getJwt())
                .when(request).getHeader("auth-token");

        UserDTO actual = QueryParameterUtil.getUser(request);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }
}