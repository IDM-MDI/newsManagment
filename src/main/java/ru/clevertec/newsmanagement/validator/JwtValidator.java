package ru.clevertec.newsmanagement.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtValidator {
    public static boolean isHeaderBearerExist(String header) {
        return Objects.nonNull(header) && header.startsWith("Bearer ");
    }

    public static boolean isUsernameExist(String username) {
        return Objects.nonNull(username) && !isSecurityAuthenticationEmpty();
    }

    public static boolean isSecurityAuthenticationEmpty() {
        return isSecurityContextEmpty() || Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
    }

    public static boolean isSecurityContextEmpty() {
        return Objects.isNull(SecurityContextHolder.getContext());
    }
}
