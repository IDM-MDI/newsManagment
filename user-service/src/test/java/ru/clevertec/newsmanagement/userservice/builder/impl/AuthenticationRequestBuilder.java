package ru.clevertec.newsmanagement.userservice.builder.impl;

import ru.clevertec.newsmanagement.userservice.builder.TestBuilder;
import ru.clevertec.newsmanagement.userservice.model.DTO;

public class AuthenticationRequestBuilder implements TestBuilder<DTO.AuthenticationRequest> {
    private String username = "test username";
    private String password = "test password";

    private AuthenticationRequestBuilder() {}

    public static AuthenticationRequestBuilder aRequest() {
        return new AuthenticationRequestBuilder();
    }

    public AuthenticationRequestBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public AuthenticationRequestBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public DTO.AuthenticationRequest build() {
        return DTO.AuthenticationRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();
    }
}
