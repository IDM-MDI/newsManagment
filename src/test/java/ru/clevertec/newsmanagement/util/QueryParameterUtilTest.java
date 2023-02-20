package ru.clevertec.newsmanagement.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.model.DTO;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QueryParameterUtilTest {
    private static final String USERNAME = "test username";
    private static final String TITLE = "test title";
    private static final String TEXT = "test text";
    private Map<String,String[]> query;
    @BeforeEach
    void setUp() {
        query = new HashMap<>();
        query.put("username", new String[] {USERNAME});
        query.put("title", new String[] {TITLE});
        query.put("text", new String[] {TEXT});
    }
    @Test
    void getCommentByQueryShouldBeCorrect() {
        //given
        DTO.Comment expected = DTO.Comment.newBuilder()
                .setUsername(USERNAME)
                .setText(TEXT)
                .build();
        //when
        DTO.Comment actual = QueryParameterUtil.getCommentByQuery(query);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getNewsByQueryShouldBeCorrect() {
        //given
        DTO.News expected = DTO.News.newBuilder()
                .setUsername(USERNAME)
                .setTitle(TITLE)
                .setText(TEXT)
                .build();

        //when
        DTO.News actual = QueryParameterUtil.getNewsByQuery(query);

        //then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getNewsByQueryShouldBeEmpty() {
        //given
        query = new HashMap<>();
        DTO.News expected = DTO.News.newBuilder().build();

        //when
        DTO.News actual = QueryParameterUtil.getNewsByQuery(query);

        //then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getCommentByQueryShouldBeEmpty() {
        //given
        query = new HashMap<>();
        DTO.Comment expected = DTO.Comment.newBuilder().build();

        //when
        DTO.Comment actual = QueryParameterUtil.getCommentByQuery(query);

        //then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getCommentByQueryShouldThrowException() {
        //given
        query = null;

        //then
        assertThatThrownBy(() -> QueryParameterUtil.getCommentByQuery(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getNewsByQueryShouldThrowException() {
        //given
        query = null;

        //then
        assertThatThrownBy(() -> QueryParameterUtil.getNewsByQuery(null))
                .isInstanceOf(NullPointerException.class);
    }
}