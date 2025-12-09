package com.example.agile.services;

import com.example.agile.DTO.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO registerUser(UserDTO userDTO);

    Optional<UserDTO> loginUser(String username, String password);

    void logoutUser(UserDTO userDTO);

    Optional<UserDTO> getUserById(long id);

    List<UserDTO> getAllUsers(); // 🔹 Récupérer tous les utilisateurs

    UserDTO updateUser(Long id, UserDTO userDTO); // 🔹 Mettre à jour un utilisateur

    boolean deleteUser(Long id); // 🔹 Supprimer un utilisateur
}
