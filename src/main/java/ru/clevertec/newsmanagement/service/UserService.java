package ru.clevertec.newsmanagement.service;


import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;


public interface UserService {
    User findUser(String username) throws CustomException;
    DTO.AuthenticationResponse registration(DTO.AuthenticationRequest authentication) throws CustomException;
    DTO.AuthenticationResponse authenticate(DTO.AuthenticationRequest authentication) throws CustomException;
}
