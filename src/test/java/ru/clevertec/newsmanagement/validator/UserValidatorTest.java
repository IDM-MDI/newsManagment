package ru.clevertec.newsmanagement.validator;

import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserValidatorTest {
    @Test
    void isUserInvalidShouldReturnFalseWithAdmin() {
        // Arrange
        User adminUser = new User("admin", "password", Role.ADMIN);
        User commentOwner = new User("john", "password", Role.SUBSCRIBER);

        // Act
        boolean result = UserValidator.isUserInvalid(commentOwner, adminUser);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void isUserInvalidShouldReturnFalseMatching() {
        // Arrange
        User matchingUser = new User("john", "password", Role.SUBSCRIBER);
        User commentOwner = new User("john", "password", Role.SUBSCRIBER);

        // Act
        boolean result = UserValidator.isUserInvalid(commentOwner, matchingUser);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void isUserInvalidShouldReturnTrue() {
        // Arrange
        User nonAdminUser = new User("mary", "password", Role.SUBSCRIBER);
        User commentOwner = new User("john", "password", Role.SUBSCRIBER);

        // Act
        boolean result = UserValidator.isUserInvalid(commentOwner, nonAdminUser);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void isUserInvalidShouldThrowsException() {
        // Assert
        assertThatThrownBy(() -> UserValidator.isUserInvalid(null, null))
                .isInstanceOf(NullPointerException.class);
    }
}