package ru.clevertec.newsmanagement.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.newsmanagement.exception.CustomException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.USER_NOT_AUTHORIZE;

@ExtendWith(MockitoExtension.class)
class JwtSecurityUtilTest {

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @Test
    void getUsernameByContextWhenAuthenticationExistsShouldReturnUsername() throws CustomException {
        // given
        String username = "test_user";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        // when
        String result = JwtSecurityUtil.getUsernameByContext();

        // then
        Assertions.assertThat(result).isEqualTo(username);
    }

    @Test
    void getUsernameByContextWhenAuthenticationDoesNotExistShouldThrowCustomException() {
        // given
        SecurityContextHolder.clearContext();

        // then
        assertThatThrownBy(JwtSecurityUtil::getUsernameByContext)
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(USER_NOT_AUTHORIZE.toString());
    }
}