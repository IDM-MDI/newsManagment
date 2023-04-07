package ru.clevertec.newsmanagement.newsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.clevertec.newsmanagement.newsservice.model.DTO;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("api/v1/auth/context")
    DTO.User userByContext();
}
