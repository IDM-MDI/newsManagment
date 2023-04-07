package ru.clevertec.newsmanagement.newsservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "ru.clevertec.newsmanagement.newsservice")
@EnableJpaRepositories(basePackages = "ru.clevertec.newsmanagement.newsservice")
@EnableJpaAuditing
@RequiredArgsConstructor
public class PersistenceConfig {
}
