package ru.clevertec.newsmanagement.newsservice.builder.impl;

import ru.clevertec.newsmanagement.newsservice.builder.TestDTOBuilder;
import ru.clevertec.newsmanagement.newsservice.model.UserDTO;

public class UserBuilder implements TestDTOBuilder<UserDTO> {

    private String username = "test username";

    private String role = "SUBSCRIBER";

    private String jwt = "test jwt";

    private UserBuilder() {}

    public static UserBuilder aUser() {
        return new UserBuilder();
    }


    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public UserBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public UserDTO buildToDTO() {
        return UserDTO.builder()
                .username(username)
                .jwt(jwt)
                .role(role)
                .build();
    }
}
