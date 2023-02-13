package ru.clevertec.newsmanagement.model;

import lombok.Data;

@Data
public class AuthenticationDto {
    private String username;
    private String password;
    private String jwt;
}
