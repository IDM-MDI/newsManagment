package ru.clevertec.newsmanagement.validator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

@NoArgsConstructor
public class UserValidator {
    public static boolean isUserValid(@NotNull User fromComment,
                                      @NotBlank User fromUsername) {
        return Role.ADMIN.equals(fromUsername.getRole()) || fromUsername.getUsername().equals(fromComment.getUsername());
    }
}
