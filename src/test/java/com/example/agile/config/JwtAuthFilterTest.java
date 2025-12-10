package com.example.agile.config;

import com.example.agile.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        userDetails = new User("testuser", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void doFilterInternal_WithNoAuthorizationHeader_ShouldContinueFilterChain() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_WithInvalidAuthorizationHeader_ShouldContinueFilterChain() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader token");

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_WithValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
        // Given
        String token = "valid-jwt-token";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtService, times(1)).extractUsername(token);
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(jwtService, times(1)).validateToken(token, userDetails);
        verify(securityContext, times(1)).setAuthentication(any(Authentication.class));
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
        // Given
        String token = "invalid-jwt-token";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(false);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtService, times(1)).extractUsername(token);
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(jwtService, times(1)).validateToken(token, userDetails);
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithExistingAuthentication_ShouldNotReauthenticate() throws ServletException, IOException {
        // Given
        String token = "valid-jwt-token";
        String authHeader = "Bearer " + token;
        Authentication existingAuth = mock(Authentication.class);

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn("testuser");

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(existingAuth);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtService, times(1)).extractUsername(token);
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(jwtService, never()).validateToken(any(), any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WhenExceptionThrown_ShouldPropagateException() throws ServletException, IOException {
        // Given
        String token = "problematic-token";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Token parsing error"));

        // When & Then
        assertThrows(RuntimeException.class, () ->
                jwtAuthFilter.doFilterInternal(request, response, filterChain)
        );

        verify(jwtService, times(1)).extractUsername(token);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithBearerTokenOnly_ShouldContinueFilterChain() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, times(1)).doFilter(request, response);
        // Le token sera une chaîne vide après substring(7)
        verify(jwtService, times(1)).extractUsername("");
    }

    @Test
    void doFilterInternal_WithValidTokenAndUserDetails_ShouldSetAuthenticationDetails() throws ServletException, IOException {
        // Given
        String token = "valid-jwt-token";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(context);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("testuser", authentication.getName());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        verify(filterChain, times(1)).doFilter(request, response);
    }
}