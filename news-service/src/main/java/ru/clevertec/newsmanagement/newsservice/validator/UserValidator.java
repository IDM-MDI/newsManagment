package ru.clevertec.newsmanagement.newsservice.validator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import ru.clevertec.newsmanagement.newsservice.model.DTO;

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
     * @param fromUsername the user associated with the username in the authentication token
     * @return true if the user is invalid, false otherwise
     */
    public static boolean isUserInvalid(@NotNull String fromComment,
                                        @NotBlank DTO.User fromUsername) {
        return !ADMIN.equals(fromUsername.getRole()) && !fromUsername.getUsername().equals(fromComment);
    }
}