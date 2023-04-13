package ru.clevertec.newsmanagement.exceptionservice.util;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsmanagement.exceptionservice.model.DTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static ru.clevertec.newsmanagement.exceptionservice.util.JsonUtil.getJSONStringException;

@ExtendWith(MockitoExtension.class)
class JsonUtilTest {
    
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
    void toJsonWithNonNullMessageOrBuilderShouldReturnJsonString() {
        // Arrange
        String expected = """
                {
                  "exception": "exception name",
                  "url": "url"
                }""";
        DTO.Exception exception = DTO.Exception.newBuilder()
                .setException("exception name")
                .setUrl("url")
                .build();

        // Act
        String jsonString = JsonUtil.toJson(exception);

        // Assert
        Assertions.assertThat(jsonString).isEqualTo(expected);
    }

    @Test
    void toJsonWithNullMessageOrBuilderShouldThrowException() {
        // Arrange
        DTO.Exception exception = null;

        // Assert
        assertThatThrownBy(() -> JsonUtil.toJson(exception)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void toJsonWithNonNullListOfMessageOrBuilderShouldReturnJsonString() {
        // Arrange
        String expected = "[\n" +
                "{\n" +
                "  \"exception\": \"exception name1\",\n" +
                "  \"url\": \"url1\"\n" +
                "},\n" +
                "{\n" +
                "  \"exception\": \"exception name2\",\n" +
                "  \"url\": \"url2\"\n" +
                "}\n" +
                "]";
        DTO.Exception exception1 = DTO.Exception.newBuilder()
                .setException("exception name1")
                .setUrl("url1")
                .build();
        DTO.Exception exception2 = DTO.Exception.newBuilder()
                .setException("exception name2")
                .setUrl("url2")
                .build();

        List<DTO.Exception> exceptions = List.of(exception1,exception2);

        // Act
        String jsonString = JsonUtil.toJson(exceptions);

        // Assert
        Assertions.assertThat(jsonString).isEqualTo(expected);
    }

    @Test
    void toJsonWithEmptyListOfMessageOrBuilderShouldReturnEmptyJsonString() {
        // Arrange
        String expected = "[]";
        List<DTO.Exception> exceptions = new ArrayList<>();

        // Act
        String jsonString = JsonUtil.toJson(exceptions);

        // Assert
        Assertions.assertThat(jsonString).isEqualTo(expected);
    }
    @Test
    void getJSONStringExceptionShouldBeCorrect() {
        // Arrange
        String url = "http://localhost:8080/test";
        String message = "test exception";

        doReturn(new StringBuffer(url)).
                when(request).getRequestURL();

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