package ru.clevertec.newsmanagement.util;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.newsmanagement.util.DtoUtil.getJSONStringException;

@ExtendWith(MockitoExtension.class)
class DtoUtilTest {
    @Mock
    private HttpServletRequest request;
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
        String expected = "[\n" +
                "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"title\": \"News 1\",\n" +
                "  \"text\": \"This is news 1.\"\n" +
                "},\n" +
                "{\n" +
                "  \"id\": \"2\",\n" +
                "  \"title\": \"News 2\",\n" +
                "  \"text\": \"This is news 2.\"\n" +
                "}\n" +
                "]";
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

    @Test
    void getJSONStringExceptionShouldBeCorrect() {
        // Arrange
        String url = "http://localhost:8080/test";
        String message = "test exception";

        when(request.getRequestURL())
                .thenReturn(new StringBuffer(url));

        // Act
        String actual = getJSONStringException(request, message);

        // Assert
        Assertions.assertThat(actual).isNotBlank();
    }

    @Test
    void getJSONStringExceptionWithEmptyRequestShouldBeThrowException() {
        // Arrange
        String message = "test exception";

        // Assert
        Assertions.assertThatThrownBy(() -> getJSONStringException(null,message))
                .isInstanceOf(NullPointerException.class);
    }
    @Test
    void getJSONStringExceptionWithEmptyMessageShouldBeThrowException() {
        // Assert
        Assertions.assertThatThrownBy(() -> getJSONStringException(request,""))
                .isInstanceOf(NullPointerException.class);
    }
}