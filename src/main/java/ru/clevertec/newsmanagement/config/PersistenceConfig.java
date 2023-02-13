package ru.clevertec.newsmanagement.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "ru.clevertec.newsmanagement")
@EnableJpaRepositories(basePackages = "ru.clevertec.newsmanagement")
@EnableJpaAuditing
public class PersistenceConfig {
}
