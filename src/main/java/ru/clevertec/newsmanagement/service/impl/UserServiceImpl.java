package ru.clevertec.newsmanagement.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.model.AuthenticationRequest;
import ru.clevertec.newsmanagement.model.AuthenticationResponse;
import ru.clevertec.newsmanagement.persistence.UserRepository;
import ru.clevertec.newsmanagement.security.JwtService;
import ru.clevertec.newsmanagement.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse registration(@Valid AuthenticationRequest authentication) {
        User user = User.builder()
                .username(authentication.getUsername())
                .password(authentication.getPassword())
                .role(Role.SUBSCRIBER)
                .build();
        User saved = repository.save(user);
        return AuthenticationResponse.builder()
                .username(saved.getUsername())
                .jwt(jwtService.generateToken(saved))
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(@Valid AuthenticationRequest authentication) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentication.getUsername(),authentication.getPassword())
        );
        User user = findByUsername(authentication.getUsername());
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .jwt(jwtService.generateToken(user))
                .build();
    }
    private User findByUsername(String username) throws Exception {
        return repository.findUserByUsername(username)
                .orElseThrow(Exception::new);           //TODO: FINISH EXCEPTION
    }
}
