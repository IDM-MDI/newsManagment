package ru.clevertec.newsmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.persistence.UserRepository;
import ru.clevertec.newsmanagement.security.JwtService;
import ru.clevertec.newsmanagement.service.UserService;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.USER_EXIST;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    @Override
    public DTO.AuthenticationResponse registration(DTO.AuthenticationRequest authentication) throws CustomException {
        if(repository.existsById(authentication.getUsername())) {
            throw new CustomException(USER_EXIST.toString());
        }
        User saved = saveDefaultUser(authentication);
        return DTO.AuthenticationResponse.newBuilder()
                .setUsername(saved.getUsername())
                .setJwt(jwtService.generateToken(saved))
                .build();
    }
    @Override
    public User findUser(String username) throws CustomException {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND.toString()));
    }

    @Override
    public DTO.AuthenticationResponse authenticate(DTO.AuthenticationRequest authentication) throws CustomException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentication.getUsername(),authentication.getPassword())
        );
        User user = findUser(authentication.getUsername());
        return DTO.AuthenticationResponse.newBuilder()
                .setUsername(user.getUsername())
                .setJwt(jwtService.generateToken(user))
                .build();
    }

    private User saveDefaultUser(DTO.AuthenticationRequest user) {
        return repository.save(User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.SUBSCRIBER)
                .build());
    }
}
