package ru.clevertec.newsmanagement.userservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.newsmanagement.exceptionservice.exception.CustomException;
import ru.clevertec.newsmanagement.userservice.entity.User;
import ru.clevertec.newsmanagement.userservice.model.DTO;
import ru.clevertec.newsmanagement.userservice.persistence.UserRepository;
import ru.clevertec.newsmanagement.userservice.service.JwtService;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.JWT_NOT_VALID;
import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.USER_EXIST;
import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.USER_NOT_FOUND;
import static ru.clevertec.newsmanagement.userservice.builder.impl.AuthenticationRequestBuilder.aRequest;
import static ru.clevertec.newsmanagement.userservice.builder.impl.AuthenticationResponseBuilder.aResponse;
import static ru.clevertec.newsmanagement.userservice.builder.impl.UserBuilder.aUser;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserServiceImpl userService;

    private DTO.AuthenticationRequest authentication;
    private DTO.AuthenticationResponse response;
    private User user;

    @BeforeEach
    public void beforeEach() {
        authentication = aRequest().build();
        response = aResponse().build();
        user = aUser().build();
    }
    @Test
    void registrationShouldBeCorrect() {
        // Arrange
        doReturn(false)
                .when(repository).existsById(authentication.getUsername());
        doReturn("encodedpassword")
                .when(passwordEncoder).encode(authentication.getPassword());
        doReturn(user).
                when(repository).save(user);
        doReturn(response.getJwt()).
                when(jwtService).generateToken(user);

        // Act
        DTO.AuthenticationResponse result = userService.registration(authentication);

        // Assert
        Assertions.assertThat(result)
                .isEqualTo(response);
    }

    @Test
    void registrationUserExists() {
        // Arrange
        doReturn(true)
                .when(repository).existsById(authentication.getUsername());

        // Assert
        Assertions.assertThatThrownBy(() -> userService.registration(authentication))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(USER_EXIST.toString());
        verify(repository).existsById(authentication.getUsername());
        verifyNoMoreInteractions(repository, jwtService, authenticationManager, passwordEncoder);
    }

    @Test
    void findUserShouldFound() throws CustomException {
        // Arrange
        doReturn(Optional.of(user)).
                when(repository).findUserByUsername(authentication.getUsername());

        // Act
        User result = userService.findUser(authentication.getUsername());

        // Assert
        Assertions.assertThat(result).isEqualTo(user);
        verify(repository).findUserByUsername(authentication.getUsername());
    }

    @Test
    void findUserShouldNotFound() {
        // Arrange
        doReturn(Optional.empty())
                .when(repository).findUserByUsername(authentication.getUsername());

        // Assert
        Assertions.assertThatThrownBy(() -> userService.findUser(authentication.getUsername()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(USER_NOT_FOUND.toString());
        verify(repository).findUserByUsername(authentication.getUsername());
        verifyNoMoreInteractions(repository, jwtService, authenticationManager, passwordEncoder);
    }

    @Test
    void authenticateShouldBeCorrect() throws CustomException {
        // Arrange
        doReturn(new UsernamePasswordAuthenticationToken(authentication.getUsername(),authentication.getPassword()))
                .when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(authentication.getUsername(),authentication.getPassword()));
        doReturn(Optional.of(user))
                .when(repository).findUserByUsername(authentication.getUsername());
        doReturn(response.getJwt())
                .when(jwtService).generateToken(user);

        // Act
        DTO.AuthenticationResponse result = userService.authenticate(authentication);

        //then
        Assertions.assertThat(result).isEqualTo(response);
    }

    @Test
    void validateTokenShouldReturnResponse() {
        String username = user.getUsername();
        String jwt = response.getJwt();

        doReturn(username)
                .when(jwtService).extractUsername(jwt);
        doReturn(user)
                .when(userDetailsService).loadUserByUsername(username);
        doReturn(true)
                .when(jwtService).isTokenValid(jwt, user);

        DTO.AuthenticationResponse actual = userService.validateToken(jwt, request);

        Assertions.assertThat(actual)
                .isEqualTo(response);
    }
    @Test
    void validateTokenShouldReturnThrowCustomException() {
        String username = user.getUsername();
        String jwt = response.getJwt();

        doReturn(username)
                .when(jwtService).extractUsername(jwt);
        doReturn(user)
                .when(userDetailsService).loadUserByUsername(username);
        doReturn(false)
                .when(jwtService).isTokenValid(jwt, user);

        Assertions.assertThatThrownBy(() -> userService.validateToken(jwt, request))
                        .isInstanceOf(CustomException.class)
                        .hasMessageContaining(JWT_NOT_VALID.toString());
    }
}