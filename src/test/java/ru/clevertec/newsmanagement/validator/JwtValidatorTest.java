package ru.clevertec.newsmanagement.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtValidatorTest {
    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }
    @Test
    void isHeaderBearerExistWithNullShouldReturnsFalse() {
        assertThat(JwtValidator.isHeaderBearerExist(null)).isFalse();
    }

    @Test
    void isHeaderBearerExistWithNonBearerHeaderShouldReturnsFalse() {
        String header = "Token abcdefg123456";
        assertThat(JwtValidator.isHeaderBearerExist(header)).isFalse();
    }

    @Test
    void isHeaderBearerExistWithBearerHeaderShouldReturnsTrue() {
        String header = "Bearer abcdefg123456";
        assertThat(JwtValidator.isHeaderBearerExist(header)).isTrue();
    }

    @Test
    void isUsernameExistWithNullUsernameShouldReturnsFalse() {
        assertThat(JwtValidator.isUsernameExist(null)).isFalse();
    }

    @Test
    void isUsernameExistWithEmptyAuthenticationShouldReturnsTrue() {
        String username = "testuser";
        assertThat(JwtValidator.isUsernameExist(username)).isTrue();
    }

    @Test
    void isUsernameExistWithNonEmptyAuthenticationShouldReturnsFalse() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = "testuser";
        assertThat(JwtValidator.isUsernameExist(username)).isFalse();
    }

    @Test
    void isSecurityAuthenticationEmptyWithNullSecurityContextShouldReturnsTrue() {
        assertThat(JwtValidator.isSecurityAuthenticationEmpty()).isTrue();
    }

    @Test
    void isSecurityAuthenticationEmptyWithEmptySecurityContextShouldReturnsTrue() {
        assertThat(JwtValidator.isSecurityAuthenticationEmpty()).isTrue();
    }

    @Test
    void isSecurityAuthenticationEmptyWithNonEmptyContextAndNullAuthShouldReturnsTrue() {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertThat(JwtValidator.isSecurityAuthenticationEmpty()).isTrue();
    }

    @Test
    void isSecurityAuthenticationEmptyWithNonEmptySecurityContextAndNonEmptyAuthenticationShouldReturnsFalse() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertThat(JwtValidator.isSecurityAuthenticationEmpty()).isFalse();
    }

    @Test
    void isSecurityContextEmptyWithNonEmptySecurityContextShouldReturnsFalse() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertThat(JwtValidator.isSecurityContextEmpty()).isFalse();
    }
}