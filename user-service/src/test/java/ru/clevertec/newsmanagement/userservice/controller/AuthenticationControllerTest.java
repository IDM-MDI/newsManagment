package ru.clevertec.newsmanagement.userservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsmanagement.userservice.UserServiceApplication;
import ru.clevertec.newsmanagement.userservice.container.PostgresTestContainer;
import ru.clevertec.newsmanagement.userservice.exception.CustomException;
import ru.clevertec.newsmanagement.userservice.model.DTO;
import ru.clevertec.newsmanagement.userservice.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.newsmanagement.userservice.exception.ExceptionStatus.USER_EXIST;
import static ru.clevertec.newsmanagement.userservice.exception.ExceptionStatus.USER_NOT_FOUND;
import static ru.clevertec.newsmanagement.userservice.util.JsonUtil.toJson;

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

    private static final String USERNAME = "username";
    private static final String PASSWORD = "test123";
    private static final String JWT = "jwt";
    private static final String ROLE = "TEST";

    private DTO.AuthenticationRequest request;
    private DTO.AuthenticationResponse response;
    @BeforeEach
    public void setup() {
        request = DTO.AuthenticationRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();
        response = DTO.AuthenticationResponse.newBuilder()
                .setUsername(USERNAME)
                .setRole(ROLE)
                .setJwt(JWT)
                .build();
    }

    @Test
    void registerShouldReturnOk() throws Exception {
        // Arrange
        when(userService.registration(request)).thenReturn(response);

        // Act
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.jwt").value(JWT));
    }
    @Test
    void registerWithExistingUsernameShouldReturnServiceUnavailable() throws Exception {
        // Arrange
        DTO.AuthenticationRequest request = DTO.AuthenticationRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();
        when(userService.registration(request)).thenThrow(new CustomException(USER_EXIST.toString()));

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
        when(userService.authenticate(request)).thenReturn(response);

        // Act
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.jwt").value(JWT));
    }

    @Test
    void authenticateWithExistingUsernameShouldReturnServiceUnavailable() throws Exception {
        // Arrange
        DTO.AuthenticationRequest request = DTO.AuthenticationRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();
        when(userService.authenticate(request)).thenThrow(new CustomException(USER_NOT_FOUND.toString()));

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
        DTO.AuthenticationRequest request = DTO.AuthenticationRequest.newBuilder()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .build();
        when(userService.authenticate(request)).thenThrow(new UsernameNotFoundException(USER_NOT_FOUND.toString()));

        // Act
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))

                // Assert
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value(USER_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.url").value("http://localhost/api/v1/auth/authenticate"));
    }
}