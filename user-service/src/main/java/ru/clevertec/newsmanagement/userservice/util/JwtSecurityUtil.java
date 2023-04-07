package ru.clevertec.newsmanagement.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.newsmanagement.userservice.entity.User;
import ru.clevertec.newsmanagement.userservice.exception.CustomException;
import ru.clevertec.newsmanagement.userservice.model.DTO;
import ru.clevertec.newsmanagement.userservice.validator.JwtValidator;

import static ru.clevertec.newsmanagement.userservice.exception.ExceptionStatus.USER_NOT_AUTHORIZE;


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
     */
    public static DTO.AuthenticationResponse getUserByContext() {
        if(JwtValidator.isSecurityAuthenticationEmpty()) {
            throw new CustomException(USER_NOT_AUTHORIZE.toString());
        }
        User authentication = (User) SecurityContextHolder.getContext().getAuthentication();
        return DTO.AuthenticationResponse.newBuilder()
                .setUsername(authentication.getUsername())
                .setRole(authentication.getRole().name())
                .build();
    }
}
