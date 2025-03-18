package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.UserDTO;
import com.example.demo_sprinboot.entities.User;
import java.util.Optional;

public interface UserService {

    UserDTO registerUser(UserDTO userDTO);

    Optional<UserDTO> loginUser(String username, String password);

    void logoutUser(UserDTO userDTO);

    Optional<UserDTO> getUserById(long id);
}
