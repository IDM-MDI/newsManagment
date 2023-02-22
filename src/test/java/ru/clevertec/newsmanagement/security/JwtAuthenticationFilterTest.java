package ru.clevertec.newsmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    private static final String TEST_JWT_TOKEN = "test.jwt.token";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_AUTH_HEADER = "Bearer " + TEST_JWT_TOKEN;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void doFilterInternalWithMissingAuthHeader() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalWithInvalidAuthHeader() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("invalid-auth-header");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalWithValidAuthHeader() throws Exception {
        // Arrange
        User user = new User(TEST_USERNAME, "password", Role.SUBSCRIBER);

        when(request.getHeader("Authorization")).thenReturn(TEST_AUTH_HEADER);
        when(jwtService.extractUsername(anyString())).thenReturn(TEST_USERNAME);
        when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(user);
        when(jwtService.isTokenValid(TEST_JWT_TOKEN, user)).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .isInstanceOf(User.class)
                .extracting("username").isEqualTo(TEST_USERNAME);
    }
}