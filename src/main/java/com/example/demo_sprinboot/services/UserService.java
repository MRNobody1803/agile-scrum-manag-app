package com.example.demo_sprinboot.services;

import java.util.Optional;
import com.example.demo_sprinboot.entities.User;
public interface UserService {
    User registerUser(User user);
    Optional<User> loginUser(String username, String password);
    void logoutUser(User user);
}