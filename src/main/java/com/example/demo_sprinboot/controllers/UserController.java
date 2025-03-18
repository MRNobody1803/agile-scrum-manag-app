package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.DTO.UserDTO;
import com.example.demo_sprinboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            return ResponseEntity.status(201).body(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            Optional<UserDTO> loggedUser = userService.loginUser(username, password);
            if (loggedUser.isPresent()) {
                return ResponseEntity.status(200).body(loggedUser.get());
            } else {
                return ResponseEntity.status(401).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestParam String username, @RequestParam String password) {
        try {
            Optional<UserDTO> userOptional = userService.loginUser(username, password);

            if (userOptional.isPresent()) {
                UserDTO userDTO = userOptional.get();

                userService.logoutUser(userDTO);

                return ResponseEntity.status(200).build();
            } else {
                return ResponseEntity.status(401).build();
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

}
