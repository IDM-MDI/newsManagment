package ru.clevertec.newsmanagement.userservice.builder.impl;

import ru.clevertec.newsmanagement.userservice.builder.TestBuilder;
import ru.clevertec.newsmanagement.userservice.entity.Role;
import ru.clevertec.newsmanagement.userservice.entity.User;

public class UserBuilder implements TestBuilder<User> {

    private String username = "test username";
    private String password = "test password";
    private Role role = Role.SUBSCRIBER;
    private UserBuilder() {}

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    @Override
    public User build() {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
