package ru.clevertec.newsmanagement.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.newsmanagement.container.PostgresTestContainer;
import ru.clevertec.newsmanagement.entity.User;

import java.util.Optional;

@ActiveProfiles({"test", "lru"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest extends PostgresTestContainer {
    @Autowired
    private UserRepository userRepository;
    @Test
    void findUserByUsernameShouldFound() {
        Optional<User> result = userRepository.findUserByUsername("admin1");

        Assertions.assertThat(result).isPresent();
    }
    @Test
    void findUserByUsernameShouldNotFound() {
        Optional<User> result = userRepository.findUserByUsername("not found");

        Assertions.assertThat(result).isNotPresent();
    }
}