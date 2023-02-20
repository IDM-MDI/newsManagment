package ru.clevertec.newsmanagement.validator;

import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserValidatorTest {
    @Test
    void isUserInvalidShouldReturnFalseWithAdmin() {
        // given
        User adminUser = new User("admin", "password", Role.ADMIN);
        User commentOwner = new User("john", "password", Role.SUBSCRIBER);

        // when
        boolean result = UserValidator.isUserInvalid(commentOwner, adminUser);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void isUserInvalidShouldReturnFalseMatching() {
        // given
        User matchingUser = new User("john", "password", Role.SUBSCRIBER);
        User commentOwner = new User("john", "password", Role.SUBSCRIBER);

        // when
        boolean result = UserValidator.isUserInvalid(commentOwner, matchingUser);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void isUserInvalidShouldReturnTrue() {
        // given
        User nonAdminUser = new User("mary", "password", Role.SUBSCRIBER);
        User commentOwner = new User("john", "password", Role.SUBSCRIBER);

        // when
        boolean result = UserValidator.isUserInvalid(commentOwner, nonAdminUser);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isUserInvalidShouldThrowsException() {
        // then
        assertThatThrownBy(() -> UserValidator.isUserInvalid(null, null))
                .isInstanceOf(NullPointerException.class);
    }
}