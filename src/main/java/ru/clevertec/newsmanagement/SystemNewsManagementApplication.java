package ru.clevertec.newsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "ru.clevertec.newsmanagement")
@EnableCaching
public class SystemNewsManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemNewsManagementApplication.class, args);
    }
}
