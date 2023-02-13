package ru.clevertec.newsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.clevertec.newsmanagement")
public class SystemNewsManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemNewsManagementApplication.class, args);
    }

}