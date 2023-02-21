package ru.clevertec.newsmanagement.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

class JwtServiceTest {
    private static final String TEST_SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY3NjkzNTg3OCwiZXhwIjoxNjc3MDIyMjc4fQ.q724wL81nu22gaxazJ5qTVxFIPH5o7apGQodH2UdACo";
    private JwtService jwtService;
    @BeforeEach
    public void setup() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService,"SECRET_KEY",TEST_SECRET_KEY);
    }

    @Test
    void generateTokenShouldGenerateTokenWithCorrectUsername() {
        // given
        User user = new User(TEST_USERNAME, "password", Role.SUBSCRIBER);

        // when
        String token = jwtService.generateToken(user);

        // then
        Assertions.assertThat(jwtService.extractUsername(token)).isEqualTo(TEST_USERNAME);
    }

    @Test
    void isTokenValidShouldReturnTrueForValidToken() {
        // given
        User user = new User(TEST_USERNAME, "password", Role.SUBSCRIBER);
        // when
        boolean isValid = jwtService.isTokenValid(TEST_TOKEN,user);

        // then
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    void extractUsernameShouldExtractUsernameFromToken() {
        // given
        User user = new User(TEST_USERNAME, "password", Role.SUBSCRIBER);

        // when
        String username = jwtService.extractUsername(TEST_TOKEN);

        // then
        Assertions.assertThat(username).isEqualTo(TEST_USERNAME);
    }

}