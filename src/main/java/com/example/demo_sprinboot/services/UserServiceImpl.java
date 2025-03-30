package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.UserDTO;
import com.example.demo_sprinboot.entities.User;
import com.example.demo_sprinboot.exceptions.UserNotFoundException;
import com.example.demo_sprinboot.mappers.UserMapper;
import com.example.demo_sprinboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE; // Utilisation du UserMapper

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists.");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        // Créer un utilisateur sans encoder le mot de passe
        User user = userMapper.userDTOToUser(userDTO);
        user = userRepository.save(user);

        return userMapper.userToUserDTO(user);
    }

    @Override
    public Optional<UserDTO> loginUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Invalid username or password.");
        }

        User user = userOptional.get();

        // Comparaison simple des mots de passe sans hashage
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        // Mapper l'utilisateur en UserDTO sans le mot de passe
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return Optional.of(userDTO);
    }

    @Override
    public void logoutUser(UserDTO userDTO) {
        System.out.println("User " + userDTO.getUsername() + " logged out successfully.");
    }

    @Override
    public Optional<UserDTO> getUserById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(userMapper::userToUserDTO);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // On va le hacher aprés

        user = userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return true;
    }
}
