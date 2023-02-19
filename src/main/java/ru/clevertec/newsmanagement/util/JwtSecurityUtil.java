package ru.clevertec.newsmanagement.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.validator.JwtValidator;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.USER_NOT_AUTHORIZE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class JwtSecurityUtil {
    public static String getUsernameByContext() throws CustomException {
        if(JwtValidator.isSecurityAuthenticationEmpty()) {
            throw new CustomException(USER_NOT_AUTHORIZE.toString());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info(user.toString());
        return user.getUsername();
    }
}
