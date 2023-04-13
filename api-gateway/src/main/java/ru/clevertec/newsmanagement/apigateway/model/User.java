package ru.clevertec.newsmanagement.apigateway.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;
    private String role;
    private String jwt;
}
