package com.example.demo_sprinboot.controllers;


import com.example.demo_sprinboot.entities.User;
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
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.status(201).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.status(501).build();
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestBody User user) {
        return ResponseEntity.status(501).build();
    }
}
