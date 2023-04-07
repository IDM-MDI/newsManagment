package ru.clevertec.newsmanagement.userservice.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.newsmanagement.userservice.exception.CustomException;
import ru.clevertec.newsmanagement.userservice.model.DTO;
import ru.clevertec.newsmanagement.userservice.validator.JwtValidator;

import static ru.clevertec.newsmanagement.userservice.exception.ExceptionStatus.USER_NOT_AUTHORIZE;


/**
 * A utility class for handling JWT security related operations.
 * @author Dayanch
 */
@UtilityClass
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GrantedAuthority grantedAuthority = authentication.getAuthorities().stream()
                .toList()
                .get(0);
        return DTO.AuthenticationResponse.newBuilder()
                .setUsername(authentication.getName())
                .setRole(grantedAuthority.getAuthority().substring(5))
                .build();
    }
}
