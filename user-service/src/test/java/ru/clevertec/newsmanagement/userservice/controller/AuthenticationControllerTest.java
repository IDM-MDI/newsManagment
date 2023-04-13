package ru.clevertec.newsmanagement.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsmanagement.exceptionservice.exception.CustomException;
import ru.clevertec.newsmanagement.userservice.UserServiceApplication;
import ru.clevertec.newsmanagement.userservice.container.PostgresTestContainer;
import ru.clevertec.newsmanagement.userservice.model.DTO;
import ru.clevertec.newsmanagement.userservice.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.JWT_NOT_VALID;
import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.USER_EXIST;
import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.USER_NOT_FOUND;
import static ru.clevertec.newsmanagement.exceptionservice.util.JsonUtil.toJson;
import static ru.clevertec.newsmanagement.userservice.builder.impl.AuthenticationRequestBuilder.aRequest;
import static ru.clevertec.newsmanagement.userservice.builder.impl.AuthenticationResponseBuilder.aResponse;

@SpringBootTest(classes = UserServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest extends PostgresTestContainer {
    @MockBean
    private UserService userService;
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private MockMvc mockMvc;

    private DTO.AuthenticationRequest request;
    private DTO.AuthenticationResponse response;

    @BeforeEach
    public void setup() {
        request = aRequest().build();
        response = aResponse().build();
    }

    @Test
    void registerShouldReturnOk() throws Exception {
        // Arrange
        doReturn(response)
                .when(userService).registration(request);

        // Act
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(response.getUsername()))
                .andExpect(jsonPath("$.jwt").value(response.getJwt()));
    }
    @Test
    void registerWithExistingUsernameShouldReturnServiceUnavailable() throws Exception {
        // Arrange
        doThrow(new CustomException(USER_EXIST.toString()))
                .when(userService).registration(request);

        // Act
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))

                // Assert
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value(USER_EXIST.toString()))
                .andExpect(jsonPath("$.url").value("http://localhost/api/v1/auth/register"));
    }

    @Test
    void authenticateShouldReturnOk() throws Exception {
        // Arrange
        doReturn(response)
                .when(userService).authenticate(request);

        // Act
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(response.getUsername()))
                .andExpect(jsonPath("$.jwt").value(response.getJwt()));
    }
    @Test
    void validateTokenShouldReturnOk() throws Exception {
        String token = "token";
        // Arrange
        doReturn(response)
                .when(userService).validateToken(eq(token),any(HttpServletRequest.class));

        // Act
        mockMvc.perform(post("/api/v1/auth/validateToken?token=" + token))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(response.getUsername()))
                .andExpect(jsonPath("$.jwt").value(response.getJwt()));
    }

    @Test
    void validateTokenShouldThrowCustomException() throws Exception {
        String token = "token";
        // Arrange
        doThrow(new CustomException(JWT_NOT_VALID.toString())).
                when(userService).validateToken(eq(token),any(HttpServletRequest.class));

        // Act
        mockMvc.perform(post("/api/v1/auth/validateToken?token=" + token))
                // Assert
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value(JWT_NOT_VALID.toString()))
                .andExpect(jsonPath("$.url").value("http://localhost/api/v1/auth/validateToken"));
    }

    @Test
    void authenticateWithExistingUsernameShouldReturnServiceUnavailable() throws Exception {
        // Arrange
        doThrow(new CustomException(USER_NOT_FOUND.toString())).
                when(userService).authenticate(request);

        // Act
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))

                // Assert
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value(USER_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.url").value("http://localhost/api/v1/auth/authenticate"));
    }
    @Test
    void authenticateWithExistingUsernameShouldReturnUsernameNotFound() throws Exception {
        // Arrange
        doThrow(new CustomException(USER_NOT_FOUND.toString()))
                .when(userService).authenticate(request);

        // Act
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))

                // Assert
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value(USER_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.url").value("http://localhost/api/v1/auth/authenticate"));
    }
}