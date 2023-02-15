package ru.clevertec.newsmanagement.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.validator.JwtValidator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtSecurityHandler {
    public static String getUsernameByContext() throws Exception {
        if(JwtValidator.isSecurityAuthenticationEmpty()) {
            throw new Exception();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getUsername();
    }
}
