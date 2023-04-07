package ru.clevertec.newsmanagement.userservice.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;


/**
 * A utility class that provides methods for validating JWT related information.
 * @author Dayanch
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtValidator {

    /**
     * Checks if the given Authorization header starts with "Bearer ".
     * @param header the Authorization header
     * @return true if the header starts with "Bearer ", false otherwise
     */
    public static boolean isHeaderBearerExist(String header) {
        return Objects.nonNull(header) && header.startsWith("Bearer ");
    }


    /**
     * Checks if the given username is not null and if the SecurityContextHolder's authentication is empty.
     * @param username the username to check
     * @return true if the username is not null and the SecurityContextHolder's authentication is empty, false otherwise
     */
    public static boolean isUsernameExist(String username) {
        return Objects.nonNull(username) && isSecurityAuthenticationEmpty();
    }


    /**
     * Checks if the SecurityContextHolder's authentication is empty.
     * @return true if the SecurityContextHolder's authentication is empty, false otherwise
     */
    public static boolean isSecurityAuthenticationEmpty() {
        return isSecurityContextEmpty() || Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
    }


    /**
     * Checks if the SecurityContextHolder is empty.
     * @return true if the SecurityContextHolder is empty, false otherwise
     */
    public static boolean isSecurityContextEmpty() {
        return Objects.isNull(SecurityContextHolder.getContext());
    }
}
