package ru.clevertec.newsmanagement.userservice.builder.impl;

import ru.clevertec.newsmanagement.userservice.builder.TestBuilder;
import ru.clevertec.newsmanagement.userservice.model.DTO;

public class AuthenticationResponseBuilder implements TestBuilder<DTO.AuthenticationResponse> {
    private String username = "test username";
    private String jwt = "jwt";
    private String role = "SUBSCRIBER";

    private AuthenticationResponseBuilder() {}

    public static AuthenticationResponseBuilder aResponse() {
        return new AuthenticationResponseBuilder();
    }

    public AuthenticationResponseBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public AuthenticationResponseBuilder setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public AuthenticationResponseBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public DTO.AuthenticationResponse build() {
        return DTO.AuthenticationResponse.newBuilder()
                .setUsername(username)
                .setJwt(jwt)
                .setRole(role)
                .build();
    }
}
