package ru.clevertec.newsmanagement.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "ru.clevertec.newsmanagement")
@EnableJpaRepositories(basePackages = "ru.clevertec.newsmanagement")
@EnableJpaAuditing
@RequiredArgsConstructor
public class PersistenceConfig {
}
