package ru.clevertec.newsmanagement.service;


import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.AuthenticationRequest;
import ru.clevertec.newsmanagement.model.AuthenticationResponse;

public interface UserService {
    User findUser(String username) throws CustomException;
    AuthenticationResponse registration(AuthenticationRequest authentication) throws CustomException;
    AuthenticationResponse authenticate(AuthenticationRequest authentication) throws CustomException;
}
