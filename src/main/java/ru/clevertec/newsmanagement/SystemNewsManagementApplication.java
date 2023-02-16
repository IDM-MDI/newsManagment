package ru.clevertec.newsmanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.clevertec.newsmanagement")
@RequiredArgsConstructor
public class SystemNewsManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemNewsManagementApplication.class, args);
    }
}
