package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.UserDTO;
import com.example.demo_sprinboot.entities.User;
import java.util.Optional;

public interface UserService {

    // Méthode pour enregistrer un nouvel utilisateur à partir d'un UserDTO
    UserDTO registerUser(UserDTO userDTO);

    // Méthode pour connecter un utilisateur avec un UserDTO
    Optional<UserDTO> loginUser(String username, String password);

    // Méthode pour déconnecter un utilisateur
    void logoutUser(UserDTO userDTO);

    // Optionnel: récupérer les informations utilisateur par ID
    Optional<UserDTO> getUserById(long id);
}
