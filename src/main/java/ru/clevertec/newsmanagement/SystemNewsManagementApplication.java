package ru.clevertec.newsmanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import ru.clevertec.newsmanagement.service.UserGenerator;

@SpringBootApplication(scanBasePackages = "ru.clevertec.newsmanagement")
@RequiredArgsConstructor
public class SystemNewsManagementApplication {
    private final UserGenerator generator;
    public static void main(String[] args) {
        SpringApplication.run(SystemNewsManagementApplication.class, args);
    }
    @EventListener(ApplicationReadyEvent.class)
    public void generateAdmins() {
        generator.generateEntity();
    }
}
