package ru.clevertec.newsmanagement.validator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

import java.util.Objects;

@NoArgsConstructor
public class UserValidator {
    public static boolean isUserValid(@NotNull User user,
                                      @NotBlank String username) {
        return user.getRole().equals(Role.ADMIN) || username.equals(user.getUsername());
    }
}
