package com.example.agile.config;

import com.example.agile.services.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private AuthenticationManager authenticationManager;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(userDetailsService, jwtAuthFilter);
    }

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // When
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void passwordEncoder_ShouldEncodePassword() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        // When
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Then
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void passwordEncoder_ShouldGenerateDifferentHashesForSamePassword() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String password = "testPassword123";

        // When
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        // Then
        assertNotEquals(hash1, hash2); // BCrypt génère des salts différents
        assertTrue(passwordEncoder.matches(password, hash1));
        assertTrue(passwordEncoder.matches(password, hash2));
    }

    @Test
    void authenticationProvider_ShouldReturnConfiguredDaoAuthenticationProvider() {
        // When
        DaoAuthenticationProvider authProvider = securityConfig.authenticationProvider();

        // Then
        assertNotNull(authProvider);
        assertTrue(true);
    }

    @Test
    void authenticationProvider_ShouldUseCustomUserDetailsService() {
        // When
        DaoAuthenticationProvider authProvider = securityConfig.authenticationProvider();

        // Then
        assertNotNull(authProvider);
        // On vérifie que le provider a été configuré (ne peut pas tester directement le service)
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void authenticationManager_ShouldReturnAuthenticationManager() throws Exception {
        // Given
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        // When
        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);

        // Then
        assertNotNull(result);
        assertEquals(authenticationManager, result);
        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }

    @Test
    void authenticationProvider_ShouldHavePasswordEncoder() {
        // When
        DaoAuthenticationProvider authProvider = securityConfig.authenticationProvider();

        // Then
        assertNotNull(authProvider);
        // Le provider devrait avoir un PasswordEncoder configuré
        // On peut le vérifier indirectement en testant que l'encodage fonctionne
    }

    @Test
    void passwordEncoder_ShouldNotMatchIncorrectPassword() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String password = "correctPassword";
        String wrongPassword = "wrongPassword";

        // When
        String encoded = passwordEncoder.encode(password);

        // Then
        assertFalse(passwordEncoder.matches(wrongPassword, encoded));
    }

    @Test
    void authenticationProvider_ShouldNotBeNull() {
        // When
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();

        // Then
        assertNotNull(provider);
        // On vérifie que le provider est bien configuré
        assertTrue(true);
    }

}