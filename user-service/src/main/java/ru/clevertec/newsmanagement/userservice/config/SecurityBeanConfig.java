package ru.clevertec.newsmanagement.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.newsmanagement.exceptionservice.exception.CustomException;
import ru.clevertec.newsmanagement.userservice.persistence.UserRepository;

import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.USER_NOT_FOUND;


/**
 * Configuration class for security-related beans.
 * @author Dayanch
 */
@Configuration
@RequiredArgsConstructor
public class SecurityBeanConfig {

    /**
     * The user repository used to retrieve user information for authentication.
     */
    private final UserRepository repository;


    /**
     * Returns a {@link UserDetailsService} that retrieves user information from the repository.
     * @return the {@link UserDetailsService}
     * @throws UsernameNotFoundException if the user is not found in the repository
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findUserByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND.toString()));
    }


    /**
     * Returns an {@link AuthenticationProvider} that uses the {@link UserDetailsService}
     * and a {@link PasswordEncoder} to perform authentication.
     * @return the {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    /**
     * Returns an {@link AuthenticationManager} that uses the provided {@link AuthenticationConfiguration}.
     * @param config the {@link AuthenticationConfiguration}
     * @return the {@link AuthenticationManager}
     * @throws Exception if an error occurs while creating the {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    /**
     * Returns a {@link PasswordEncoder} that uses the BCrypt algorithm.
     * @return the {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
