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


/**
 * A utility class for handling JWT security related operations.
 * @author Dayanch
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class JwtSecurityUtil {

    /**
     * Retrieves the username of the currently authenticated user from the security context.
     * @return the username of the currently authenticated user
     * @throws CustomException if the user is not authorized or the authentication information is empty
     */
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
