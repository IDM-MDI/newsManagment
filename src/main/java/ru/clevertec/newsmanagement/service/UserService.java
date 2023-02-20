package ru.clevertec.newsmanagement.service;


import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;


/**
 * Service interface for User-related operations.
 * @author Dayanch
 */
public interface UserService {
    /**
     * Retrieves a user by their username.
     * @param username the username to search for
     * @return the User object matching the given username
     * @throws CustomException if the user is not found
     */
    User findUser(String username) throws CustomException;

    /**
     * Registers a new user with the provided authentication information.
     * @param authentication the authentication information for the new user
     * @return an AuthenticationResponse containing the JWT and the username of the newly registered user
     * @throws CustomException if the username is already taken or if there is an error during registration
     */
    DTO.AuthenticationResponse registration(DTO.AuthenticationRequest authentication) throws CustomException;

    /**
     * Authenticates a user with the provided authentication information.
     * @param authentication the authentication information for the user
     * @return an AuthenticationResponse containing the JWT and the username of the authenticated user
     * @throws CustomException if the authentication information is invalid or if there is an error during authentication
     */
    DTO.AuthenticationResponse authenticate(DTO.AuthenticationRequest authentication) throws CustomException;
}