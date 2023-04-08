package ru.clevertec.newsmanagement.userservice.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {
    private static final String DATABASE_NAME = "test";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Container
    public static PostgreSQLContainer<PostgresTestContainer> container = new PostgresTestContainer()
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",container::getJdbcUrl);
        registry.add("spring.datasource.username",container::getUsername);
        registry.add("spring.datasource.password",container::getPassword);
        registry.add("spring.liquibase.url",container::getJdbcUrl);
        registry.add("spring.liquibase.user",container::getUsername);
        registry.add("spring.liquibase.password",container::getPassword);
    }
}
