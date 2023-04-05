package ru.clevertec.newsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "ru.clevertec.newsmanagement")
@EnableAspectJAutoProxy
public class SystemNewsManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemNewsManagementApplication.class, args);
    }
}
