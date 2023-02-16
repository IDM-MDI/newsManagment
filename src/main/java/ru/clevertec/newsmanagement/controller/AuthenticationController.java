package ru.clevertec.newsmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.AuthenticationRequest;
import ru.clevertec.newsmanagement.model.AuthenticationResponse;
import ru.clevertec.newsmanagement.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService service;

    @Operation(
            summary = "User registration",
            description = "API Point made for save user data to database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User created"
    )
    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody @Valid AuthenticationRequest request) throws CustomException {
        return service.registration(request);
    }
    @Operation(
            summary = "User authentication",
            description = "API Point made for authenticate existed user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User authenticated"
    )
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequest request) throws CustomException {
        return service.authenticate(request);
    }
}
