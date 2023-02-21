package ru.clevertec.newsmanagement.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.persistence.UserRepository;
import ru.clevertec.newsmanagement.security.JwtService;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.USER_EXIST;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.USER_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;
    private DTO.AuthenticationRequest authentication;
    private DTO.AuthenticationResponse response;
    private User user;

    @BeforeEach
    public void beforeEach() {
        authentication = DTO.AuthenticationRequest.newBuilder()
                .setUsername("testuser")
                .setPassword("testpassword")
                .build();
        response = DTO.AuthenticationResponse.newBuilder()
                .setUsername("testuser")
                .setJwt("jwt")
                .build();
        user = User.builder()
                .username("testuser")
                .password("encodedpassword")
                .role(Role.SUBSCRIBER)
                .build();
    }
    @Test
    void registrationShouldBeCorrect() throws CustomException {
        // given
        when(repository.existsById("testuser")).thenReturn(false);
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedpassword");
        when(repository.save(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwt");

        // when
        DTO.AuthenticationResponse result = userService.registration(authentication);

        // then
        Assertions.assertThat(result).isEqualTo(response);
        verify(repository).existsById("testuser");
        verify(passwordEncoder).encode("testpassword");
        verify(repository).save(user);
        verify(jwtService).generateToken(user);
    }

    @Test
    void registrationUserExists() {
        // given
        when(repository.existsById(authentication.getUsername())).thenReturn(true);

        // then
        Assertions.assertThatThrownBy(() -> userService.registration(authentication))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(USER_EXIST.toString());
        verify(repository).existsById(authentication.getUsername());
        verifyNoMoreInteractions(repository, jwtService, authenticationManager, passwordEncoder);
    }

    @Test
    void findUserShouldFound() throws CustomException {
        // given
        when(repository.findUserByUsername(authentication.getUsername())).thenReturn(Optional.of(user));

        // Act
        User result = userService.findUser(authentication.getUsername());

        // Assert
        Assertions.assertThat(result).isEqualTo(user);
        verify(repository).findUserByUsername(authentication.getUsername());
    }

    @Test
    void findUserShouldNotFound() {
        // given
        when(repository.findUserByUsername(authentication.getUsername())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> userService.findUser(authentication.getUsername()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(USER_NOT_FOUND.toString());
        verify(repository).findUserByUsername(authentication.getUsername());
        verifyNoMoreInteractions(repository, jwtService, authenticationManager, passwordEncoder);
    }

    @Test
    void authenticateShouldBeCorrect() throws CustomException {
        // given
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentication.getUsername(),authentication.getPassword())))
                .thenReturn(new UsernamePasswordAuthenticationToken(authentication.getUsername(),authentication.getPassword()));
        when(repository.findUserByUsername(authentication.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt");

        // when
        DTO.AuthenticationResponse result = userService.authenticate(authentication);

        //then
        Assertions.assertThat(result).isEqualTo(response);
    }
}