package ru.clevertec.newsmanagement.newsservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient {

}
