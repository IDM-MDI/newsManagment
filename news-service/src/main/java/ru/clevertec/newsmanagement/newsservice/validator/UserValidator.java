package ru.clevertec.newsmanagement.newsservice.validator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import ru.clevertec.newsmanagement.newsservice.model.UserDTO;

/**
 * Utility class with static methods to validate user objects.
 * @author Dayanch
 */
@UtilityClass
public class UserValidator {
    private static String ADMIN = "ADMIN";
    /**
     * Checks if a user is invalid based on their roles and username.
     * A user is invalid if they are not an admin and their username does not match that of the comment owner.
     *
     * @param fromComment the user who made the comment
     * @param fromContext the user associated with the username in the authentication token
     * @return true if the user is invalid, false otherwise
     */
    public static boolean isUserInvalid(@NotNull String fromComment,
                                        @NotBlank UserDTO fromContext) {
        return !ADMIN.equals(fromContext.getRole()) && !fromContext.getUsername().equals(fromComment);
    }
}