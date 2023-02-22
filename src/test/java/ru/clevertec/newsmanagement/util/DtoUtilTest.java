package ru.clevertec.newsmanagement.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DtoUtilTest {

    private List<String> page;
    @BeforeEach
    void setUp() {
        page = new ArrayList<>();
        page.add("element1");
        page.add("element2");
    }
    @Test
    void checkPageListExistWithNonNullListShouldNotThrowException() {
        // Assert
        Assertions.assertThatCode(() -> DtoUtil.checkPageListExist(page)).doesNotThrowAnyException();
    }

    @Test
    void checkPageListExistWithNullListShouldThrowException() {
        // Assert
        Assertions.assertThatThrownBy(() -> DtoUtil.checkPageListExist(null)).isInstanceOf(CustomException.class);
    }

    @Test
    void checkPageListExistWithEmptyListShouldThrowException() {
        // Arrange
        List<String> page = new ArrayList<>();

        // Assert
        Assertions.assertThatThrownBy(() -> DtoUtil.checkPageListExist(page)).isInstanceOf(CustomException.class);
    }

    @Test
    void toJsonWithNonNullMessageOrBuilderShouldReturnJsonString() {
        // Arrange
        String expected = """
                {
                  "id": "1",
                  "text": "This is a comment.",
                  "username": "Test"
                }""";
        DTO.Comment comment = DTO.Comment.newBuilder()
                .setId(1)
                .setUsername("Test")
                .setText("This is a comment.")
                .build();

        // Act
        String jsonString = DtoUtil.toJson(comment);

        // Assert
        Assertions.assertThat(jsonString).isEqualTo(expected);
    }

    @Test
    void toJsonWithNullMessageOrBuilderShouldThrowException() {
        // Arrange
        DTO.Comment comment = null;

        // Assert
        assertThatThrownBy(() -> DtoUtil.toJson(comment)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void toJsonWithNonNullListOfMessageOrBuilderShouldReturnJsonString() {
        // Arrange
        String expected = """
                [{
                  "id": "1",
                  "title": "News 1",
                  "text": "This is news 1."
                }{
                  "id": "2",
                  "title": "News 2",
                  "text": "This is news 2."
                }]""";
        DTO.News news1 = DTO.News.newBuilder()
                .setId(1)
                .setTitle("News 1")
                .setText("This is news 1.")
                .build();

        DTO.News news2 = DTO.News.newBuilder()
                .setId(2)
                .setTitle("News 2")
                .setText("This is news 2.")
                .build();

        List<DTO.News> newsList = new ArrayList<>();
        newsList.add(news1);
        newsList.add(news2);

        // Act
        String jsonString = DtoUtil.toJson(newsList);

        // Assert
        Assertions.assertThat(jsonString).isEqualTo(expected);
    }

    @Test
    void toJsonWithEmptyListOfMessageOrBuilderShouldReturnEmptyJsonString() {
        // Arrange
        String expected = "[]";
        List<DTO.News> newsList = new ArrayList<>();

        // Act
        String jsonString = DtoUtil.toJson(newsList);

        // Assert
        Assertions.assertThat(jsonString).isEqualTo(expected);
    }

    @Test
    void toJsonWithNullListOfMessageOrBuilderShouldThrowException() {
        // Arrange
        List<DTO.News> newsList = null;

        // Assert
        assertThatThrownBy(() -> DtoUtil.toJson(newsList)).isInstanceOf(NullPointerException.class);
    }
}