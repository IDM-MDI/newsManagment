package ru.clevertec.newsmanagement.userservice.service;


import jakarta.servlet.http.HttpServletRequest;
import ru.clevertec.newsmanagement.userservice.entity.User;
import ru.clevertec.newsmanagement.userservice.model.DTO;


/**
 * Service interface for User-related operations.
 * @author Dayanch
 */
public interface UserService {

    /**
     * Retrieves a user by their username.
     * @param username the username to search for
     * @return the User object matching the given username
     */
    User findUser(String username);

    /**
     * Registers a new user with the provided authentication information.
     * @param authentication the authentication information for the new user
     * @return an AuthenticationResponse containing the JWT and the username of the newly registered user
     */
    DTO.AuthenticationResponse registration(DTO.AuthenticationRequest authentication);

    /**
     * Authenticates a user with the provided authentication information.
     * @param authentication the authentication information for the user
     * @return an AuthenticationResponse containing the JWT and the username of the authenticated user
     */
    DTO.AuthenticationResponse authenticate(DTO.AuthenticationRequest authentication);

    DTO.AuthenticationResponse validateToken(String token, HttpServletRequest request);
}