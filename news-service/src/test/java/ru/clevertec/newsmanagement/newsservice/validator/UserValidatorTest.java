package ru.clevertec.newsmanagement.newsservice.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.newsservice.model.UserDTO;

import static ru.clevertec.newsmanagement.newsservice.builder.impl.UserBuilder.aUser;

class UserValidatorTest {
    @Test
    void isUserInvalidShouldReturnFalseByUsername() {
        UserDTO user = aUser().buildToDTO();

        boolean actual = UserValidator.isUserInvalid(user.getUsername(), user);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @Test
    void isUserInvalidShouldReturnFalseByRole() {
        String username = "user";
        UserDTO user = aUser().setRole("ADMIN").buildToDTO();

        boolean actual = UserValidator.isUserInvalid(username, user);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @Test
    void isUserInvalidShouldReturnTrue() {
        String username = "user";
        UserDTO user = aUser().buildToDTO();

        boolean actual = UserValidator.isUserInvalid(username, user);

        Assertions.assertThat(actual)
                .isTrue();
    }
}