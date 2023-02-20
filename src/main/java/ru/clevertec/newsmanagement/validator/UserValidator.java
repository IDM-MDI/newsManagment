package ru.clevertec.newsmanagement.validator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

/**
 * Utility class with static methods to validate user objects.
 * @author Dayanch
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserValidator {

    /**
     * Checks if a user is invalid based on their roles and username.
     * A user is invalid if they are not an admin and their username does not match that of the comment owner.
     *
     * @param fromComment the user who made the comment
     * @param fromUsername the user associated with the username in the authentication token
     * @return true if the user is invalid, false otherwise
     */
    public static boolean isUserInvalid(@NotNull User fromComment,
                                        @NotBlank User fromUsername) {
        return !Role.ADMIN.equals(fromUsername.getRole()) && !fromUsername.getUsername().equals(fromComment.getUsername());
    }
}