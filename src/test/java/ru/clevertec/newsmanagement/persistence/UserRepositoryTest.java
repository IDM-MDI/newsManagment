package ru.clevertec.newsmanagement.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void setup() {
        userRepository.save(
                User.builder()
                        .username("user")
                        .password("password")
                        .role(Role.SUBSCRIBER)
                        .build()
        );
    }
    @Test
    void findUserByUsernameShouldFound() {
        Optional<User> result = userRepository.findUserByUsername("user");

        Assertions.assertThat(result).isPresent();
    }
    @Test
    void findUserByUsernameShouldNotFound() {
        Optional<User> result = userRepository.findUserByUsername("not found");

        Assertions.assertThat(result).isNotPresent();
    }
}