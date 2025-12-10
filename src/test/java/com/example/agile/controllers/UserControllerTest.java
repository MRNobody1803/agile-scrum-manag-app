package com.example.agile.controllers;

import com.example.agile.dto.UserDTO;
import com.example.agile.exceptions.GlobalExceptionHandler;
import com.example.agile.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        // Initialisation d'un UserDTO pour les tests
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
    }

    @Test
    void registerUser_ShouldReturnCreatedUser() throws Exception {
        // Given
        when(userService.registerUser(any(UserDTO.class))).thenReturn(userDTO);

        // When & Then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void registerUser_WithExistingUsername_ShouldReturnBadRequest() throws Exception {
        // Given
        when(userService.registerUser(any(UserDTO.class)))
                .thenThrow(new RuntimeException("Username already exists"));

        // When & Then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void loginUser_ShouldReturnUser() throws Exception {
        // Given
        String username = "testuser";
        String password = "password123";
        when(userService.loginUser(username, password)).thenReturn(Optional.of(userDTO));

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")));

        verify(userService, times(1)).loginUser(username, password);
    }

    @Test
    void loginUser_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given
        String username = "testuser";
        String password = "wrongpassword";
        when(userService.loginUser(username, password)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).loginUser(username, password);
    }

    @Test
    void logoutUser_ShouldReturnOk() throws Exception {
        // Given
        String username = "testuser";
        String password = "password123";
        when(userService.loginUser(username, password)).thenReturn(Optional.of(userDTO));
        doNothing().when(userService).logoutUser(any(UserDTO.class));

        // When & Then
        mockMvc.perform(post("/api/users/logout")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk());

        verify(userService, times(1)).loginUser(username, password);
        verify(userService, times(1)).logoutUser(any(UserDTO.class));
    }

    @Test
    void logoutUser_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given
        String username = "testuser";
        String password = "wrongpassword";
        when(userService.loginUser(username, password)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/users/logout")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).loginUser(username, password);
        verify(userService, never()).logoutUser(any(UserDTO.class));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        // Given
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(Optional.of(userDTO));

        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long userId = 999L;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Given
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setUsername("testuser2");
        userDTO2.setEmail("test2@example.com");

        List<UserDTO> users = Arrays.asList(userDTO, userDTO2);
        when(userService.getAllUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("testuser2")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        // Given
        Long userId = 1L;
        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(userId);
        updatedDTO.setUsername("updateduser");
        updatedDTO.setEmail("updated@example.com");

        when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(updatedDTO);

        // When & Then
        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("updateduser")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));

        verify(userService, times(1)).updateUser(eq(userId), any(UserDTO.class));
    }

    @Test
    void updateUser_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long userId = 999L;
        when(userService.updateUser(eq(userId), any(UserDTO.class)))
                .thenThrow(new RuntimeException("User not found"));

        // When & Then
        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(userId), any(UserDTO.class));
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception {
        // Given
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUser_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long userId = 999L;
        when(userService.deleteUser(userId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void loginUser_WithException_ShouldReturnInternalServerError() throws Exception {
        // Given
        String username = "testuser";
        String password = "password123";
        when(userService.loginUser(username, password))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).loginUser(username, password);
    }
}