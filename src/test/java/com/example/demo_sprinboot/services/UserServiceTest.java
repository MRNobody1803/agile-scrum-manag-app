package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.UserDTO;
import com.example.demo_sprinboot.entities.User;
import com.example.demo_sprinboot.exceptions.UserNotFoundException;
import com.example.demo_sprinboot.mappers.UserMapper;
import com.example.demo_sprinboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("password");
    }

    @Test
    @DisplayName("Test user registration with valid data")
    void testRegisterUser() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userMapper.userDTOToUser(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.registerUser(userDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Test user registration with username already exist")
    void testRegisterUser_UsernameAlreadyExists() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(userDTO));
        assertEquals("Username already exists.", exception.getMessage());
    }

    @Test
    @DisplayName("Test user login with valid credentials")
    void testLoginUser_Success() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.loginUser(userDTO.getUsername(), userDTO.getPassword());

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    @DisplayName("Test user login with invalid username")
    void testLoginUser_InvalidUsername() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.loginUser(userDTO.getUsername(), userDTO.getPassword()));
        assertEquals("Invalid username or password.", exception.getMessage());
    }

    @Test
    @DisplayName("Test user login with invalid password")
    void testLoginUser_InvalidPassword() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.loginUser(userDTO.getUsername(), "wrongpassword"));
        assertEquals("Invalid username or password.", exception.getMessage());
    }

    @Test
    @DisplayName("Test user extraction with valid ID")
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    @DisplayName("Test user extraction with non-existing user")
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test user update with valid data")
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(1L, userDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Test user update with non-existing user")
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, userDTO));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Test user deletion ")
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
    }
}
