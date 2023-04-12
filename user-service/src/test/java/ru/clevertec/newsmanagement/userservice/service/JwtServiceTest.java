package ru.clevertec.newsmanagement.userservice.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.clevertec.newsmanagement.userservice.entity.User;

import static ru.clevertec.newsmanagement.userservice.builder.impl.UserBuilder.aUser;

class JwtServiceTest {
    private static final String TEST_SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final String TEST_USERNAME = "testuser";
    private String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY3NjkzNTg3OCwiZXhwIjoxNjc3MDIyMjc4fQ.q724wL81nu22gaxazJ5qTVxFIPH5o7apGQodH2UdACo";
    private User user;
    private JwtService jwtService;
    @BeforeEach
    public void setup() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService,"SECRET_KEY",TEST_SECRET_KEY);
        user = aUser().setUsername(TEST_USERNAME).build();
        TEST_TOKEN = jwtService.generateToken(user);
    }

    @Test
    void generateTokenShouldGenerateTokenWithCorrectUsername() {
        // Act
        String token = jwtService.generateToken(user);

        // Assert
        Assertions.assertThat(jwtService.extractUsername(token)).isEqualTo(TEST_USERNAME);
    }

    @Test
    void isTokenValidShouldReturnTrueForValidToken() {
        // Act
        boolean isValid = jwtService.isTokenValid(TEST_TOKEN,user);

        // Assert
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    void extractUsernameShouldExtractUsernameFromToken() {
        // Act
        String username = jwtService.extractUsername(TEST_TOKEN);

        // Assert
        Assertions.assertThat(username).isEqualTo(TEST_USERNAME);
    }
}