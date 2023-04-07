package ru.clevertec.newsmanagement.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.newsmanagement.userservice.entity.Role;
import ru.clevertec.newsmanagement.userservice.entity.User;
import ru.clevertec.newsmanagement.userservice.exception.CustomException;
import ru.clevertec.newsmanagement.userservice.model.DTO;
import ru.clevertec.newsmanagement.userservice.persistence.UserRepository;
import ru.clevertec.newsmanagement.userservice.service.JwtService;
import ru.clevertec.newsmanagement.userservice.service.UserService;

import static ru.clevertec.newsmanagement.userservice.exception.ExceptionStatus.JWT_NOT_VALID;
import static ru.clevertec.newsmanagement.userservice.exception.ExceptionStatus.USER_EXIST;
import static ru.clevertec.newsmanagement.userservice.exception.ExceptionStatus.USER_NOT_FOUND;


/**
 * Service implementation for User-related operations.
 * @author Dayanch
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService detailsService;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    /**
     * {@inheritDoc}
     */
    @Override
    public DTO.AuthenticationResponse registration(DTO.AuthenticationRequest authentication) {
        if(repository.existsById(authentication.getUsername())) {
            throw new CustomException(USER_EXIST.toString());
        }
        User saved = saveDefaultUser(authentication);
        return DTO.AuthenticationResponse.newBuilder()
                .setUsername(saved.getUsername())
                .setJwt(jwtService.generateToken(saved))
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public User findUser(String username) throws CustomException {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND.toString()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DTO.AuthenticationResponse authenticate(DTO.AuthenticationRequest authentication) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentication.getUsername(),authentication.getPassword())
        );
        User user = findUser(authentication.getUsername());
        return DTO.AuthenticationResponse.newBuilder()
                .setUsername(user.getUsername())
                .setJwt(jwtService.generateToken(user))
                .build();
    }
    //TODO: JAVADOC
    @Override
    public DTO.AuthenticationResponse validateToken(String token) {
        User user = (User) detailsService.loadUserByUsername(jwtService.extractUsername(token));
        if (!jwtService.isTokenValid(token, user)) {
            throw new CustomException(JWT_NOT_VALID.toString());
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return DTO.AuthenticationResponse.newBuilder()
                .setUsername(user.getUsername())
                .setRole(user.getRole().name())
                .setJwt(token)
                .build();
    }


    /**
     * Create user from client DTO, fill it with default role(subscriber) and encode password with PasswordEncoder
     * @param user is data received from client
     * @return User which goes to the database
     */
    private User saveDefaultUser(DTO.AuthenticationRequest user) {
        return repository.save(User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.SUBSCRIBER)
                .build());
    }
}