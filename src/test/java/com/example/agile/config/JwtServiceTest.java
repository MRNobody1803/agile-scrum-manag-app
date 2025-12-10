package com.example.agile.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;
    private String secretKey;

    @BeforeEach
    void setUp() {
        // Configuration d'une clé secrète pour les tests (doit être en Base64 et >= 256 bits)
        secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);

        // Création d'un UserDetails pour les tests
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        userDetails = new User("testuser", "password", Collections.singletonList(authority));
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT format: header.payload.signature
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        String extractedUsername = jwtService.extractUsername(token);

        // Then
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidUsername_ShouldReturnFalse() {
        // Given
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = new User("differentuser", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // When
        boolean isValid = jwtService.validateToken(token, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_WithValidToken_ShouldReturnFalse() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        boolean isExpired = jwtService.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }


    @Test
    void generateToken_ShouldIncludeRoleClaim() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Then
        assertEquals("ROLE_USER", claims.get("role"));
    }

    @Test
    void generateToken_ShouldSetCorrectExpirationTime() {
        // Given
        long beforeGeneration = System.currentTimeMillis();
        String token = jwtService.generateToken(userDetails);
        long afterGeneration = System.currentTimeMillis();

        // When
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();
        long expirationTime = expiration.getTime();

        // Then
        long expectedExpiration = 1000L * 60 * 60 * 24; // 1 day
        assertFalse(expirationTime >= beforeGeneration + expectedExpiration);
        assertTrue(expirationTime <= afterGeneration + expectedExpiration + 1000); // +1s de marge
    }

    @Test
    void extractUsername_WithInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    void validateToken_WithMalformedToken_ShouldThrowException() {
        // Given
        String malformedToken = "malformed-token";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.validateToken(malformedToken, userDetails));
    }

    @Test
    void generateToken_ForDifferentUsers_ShouldGenerateDifferentTokens() {
        // Given
        UserDetails user1 = new User("user1", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        UserDetails user2 = new User("user2", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        // When
        String token1 = jwtService.generateToken(user1);
        String token2 = jwtService.generateToken(user2);

        // Then
        assertNotEquals(token1, token2);
        assertEquals("user1", jwtService.extractUsername(token1));
        assertEquals("user2", jwtService.extractUsername(token2));
    }
}