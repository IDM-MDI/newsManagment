package ru.clevertec.newsmanagement.userservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;


/**
 * This class is responsible for creating and validating JWT tokens.
 * @author Dayanch
 */
@Service
@Slf4j
public class JwtService {

    /**
     * A secret key used to sign the JWT token.
     */
    @Value("${jwt.key}")
    private String SECRET_KEY;


    /**
     * Extracts the username from the given JWT token.
     * @param token the JWT token from which the username will be extracted
     * @return the username extracted from the JWT token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Extracts the claim of the given function from the given JWT token.
     * @param token the JWT token from which the claim will be extracted
     * @param claimsResolver the function that will extract the claim from the token
     * @param <T> the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Generates a JWT token for the given user details.
     * @param userDetails the user details for which the JWT token will be generated
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Validates the given JWT token for the given user details.
     * @param token the JWT token to be validated
     * @param userDetails the user details for which the token will be validated
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    /**
     * Checks if the given JWT token is expired.
     * @param token the JWT token to be checked
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /**
     * Extracts the expiration date from the given JWT token.
     * @param token the JWT token from which the expiration date will be extracted
     * @return the expiration date extracted from the JWT token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    /**
     * Extracts all claims from the given JWT token.
     * @param token the JWT token from which the claims will be extracted
     * @return the claims extracted from the JWT token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * Gets the signing key used to sign the JWT token.
     * @return the signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
