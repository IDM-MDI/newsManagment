package ru.clevertec.newsmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.service.UserService;

import static ru.clevertec.newsmanagement.util.DtoUtil.toJson;

/**
 * REST controller for handling user authentication and registration requests
 * @author Dayanch
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService service;

    /**
     * Endpoint for registering a new user
     * @param request the authentication request containing user credentials
     * @return a JSON string representation of the newly registered user
     * @throws CustomException if an error occurs during registration
     */
    @Operation(
            summary = "User registration",
            description = "API Point made for save user data to database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User created"
    )
    @PostMapping(value = "/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public String register(@RequestBody @Valid DTO.AuthenticationRequest request) throws CustomException {
        return toJson(service.registration(request));
    }


    /**
     * Endpoint for authenticating an existing user
     * @param request the authentication request containing user credentials
     * @return a JSON string representation of the authenticated user
     * @throws CustomException if an error occurs during authentication
     */
    @Operation(
            summary = "User authentication",
            description = "API Point made for authenticate existed user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User authenticated"
    )
    @PostMapping(value = "/authenticate",produces = MediaType.APPLICATION_JSON_VALUE)
    public String authenticate(@RequestBody @Valid DTO.AuthenticationRequest request) throws CustomException {
        return toJson(service.authenticate(request));
    }
}