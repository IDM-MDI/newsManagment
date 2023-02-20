package ru.clevertec.newsmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.newsmanagement.entity.User;

import java.io.IOException;

import static ru.clevertec.newsmanagement.validator.JwtValidator.isHeaderBearerExist;
import static ru.clevertec.newsmanagement.validator.JwtValidator.isUsernameExist;


/**
 * JwtAuthenticationFilter is a filter that intercepts incoming requests and checks if there is a valid JWT token
 * in the "Authorization" header. If the token is valid, the filter sets the authenticated user in the security context
 * for the request.
 * @author Dayanch
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Intercept the incoming request and extract the JWT token from the "Authorization" header. If a valid token is
     * found, set the authenticated user in the security context for the request.
     * @param request the incoming request
     * @param response the response to the request
     * @param filterChain the filter chain
     * @throws ServletException if the request could not be processed
     * @throws IOException if an I/O error occurs while processing the request
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (!isHeaderBearerExist(authHeader)) {
            log.info("Bearer doesnt exist");
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        log.info("Username: " + username);
        if (isUsernameExist(username)) {
            User user = (User) userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, user)) {
                log.info(user.getAuthorities().toString());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
