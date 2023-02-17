package ru.clevertec.newsmanagement.validator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserValidator {
    public static boolean isUserInvalid(@NotNull User fromComment,
                                        @NotBlank User fromUsername) {
        return !Role.ADMIN.equals(fromUsername.getRole()) && !fromUsername.getUsername().equals(fromComment.getUsername());
    }
}
