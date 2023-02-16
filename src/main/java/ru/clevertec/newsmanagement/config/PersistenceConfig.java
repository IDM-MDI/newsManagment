package ru.clevertec.newsmanagement.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EntityScan(basePackages = "ru.clevertec.newsmanagement")
@EnableJpaRepositories(basePackages = "ru.clevertec.newsmanagement")
@EnableJpaAuditing
@RequiredArgsConstructor
public class PersistenceConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
