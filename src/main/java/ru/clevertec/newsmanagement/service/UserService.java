package ru.clevertec.newsmanagement.service;


import ru.clevertec.newsmanagement.model.AuthenticationRequest;
import ru.clevertec.newsmanagement.model.AuthenticationResponse;

public interface UserService {
    AuthenticationResponse registration(AuthenticationRequest authentication);
    AuthenticationResponse authenticate(AuthenticationRequest authentication) throws Exception;
}
