package ru.clevertec.newsmanagement.userservice.util;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsmanagement.exceptionservice.util.JsonUtil;
import ru.clevertec.newsmanagement.userservice.model.DTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

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
    @Nested
    class ToJson {
        @Test
        void toJsonWithNonNullMessageOrBuilderShouldReturnJsonString() {
            // Arrange
            String expected = """
                {
                  "username": "user",
                  "role": "role",
                  "jwt": "random jwt token"
                }""";
            DTO.AuthenticationResponse response = DTO.AuthenticationResponse.newBuilder()
                    .setUsername("user")
                    .setRole("role")
                    .setJwt("random jwt token")
                    .build();

            // Act
            String jsonString = JsonUtil.toJson(response);

            // Assert
            Assertions.assertThat(jsonString).isEqualTo(expected);
        }

        @Test
        void toJsonWithNullMessageOrBuilderShouldThrowException() {
            // Arrange
            DTO.AuthenticationResponse response = null;

            // Assert
            assertThatThrownBy(() -> JsonUtil.toJson(response)).isInstanceOf(NullPointerException.class);
        }
    }
    @Nested
    class JsonException {

    }
    @Test
    void getJSONStringExceptionShouldBeCorrect() {
        // Arrange
        String url = "http://localhost:8080/test";
        String message = "test exception";

        doReturn(new StringBuffer(url))
                .when(request).getRequestURL();
        // Act
        String actual = JsonUtil.getJSONStringException(request, message);

        // Assert
        Assertions.assertThat(actual).isNotBlank();
    }

    @Test
    void getJSONStringExceptionWithEmptyRequestShouldBeThrowException() {
        // Arrange
        String message = "test exception";

        // Assert
        Assertions.assertThatThrownBy(() -> JsonUtil.getJSONStringException(null,message))
                .isInstanceOf(NullPointerException.class);
    }
    @Test
    void getJSONStringExceptionWithEmptyMessageShouldBeThrowException() {
        // Assert
        Assertions.assertThatThrownBy(() -> JsonUtil.getJSONStringException(request,""))
                .isInstanceOf(NullPointerException.class);
    }
}