package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.DTO.UserDTO;
import com.example.demo_sprinboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Enregistrement d'un utilisateur
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            return ResponseEntity.status(201).body(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    // Connexion d'un utilisateur
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            Optional<UserDTO> loggedUser = userService.loginUser(username, password);
            return loggedUser.map(user -> ResponseEntity.ok().body(user))
                    .orElseGet(() -> ResponseEntity.status(401).build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Déconnexion d'un utilisateur
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestParam String username, @RequestParam String password) {
        try {
            Optional<UserDTO> userOptional = userService.loginUser(username, password);

            if (userOptional.isPresent()) {
                userService.logoutUser(userOptional.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(401).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).build();
        }
    }

    // 🔹 Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }

    // 🔹 Récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 🔹 Mettre à jour un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔹 Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}

