package ru.clevertec.newsmanagement.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AuthenticationResponse registration(@Valid AuthenticationRequest authentication) {
        User saved = saveDefaultUser(authentication);
        return AuthenticationResponse.builder()
                .username(saved.getUsername())
                .jwt(jwtService.generateToken(saved))
                .build();
    }
    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }
    private User saveDefaultUser(AuthenticationRequest user) {
        return repository.save(User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.SUBSCRIBER)
                .build());
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
