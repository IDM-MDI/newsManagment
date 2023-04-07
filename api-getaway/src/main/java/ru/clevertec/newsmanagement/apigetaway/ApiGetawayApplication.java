package ru.clevertec.newsmanagement.apigetaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGetawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGetawayApplication.class, args);
    }

}
