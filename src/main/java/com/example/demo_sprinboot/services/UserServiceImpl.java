package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.UserDTO;
import com.example.demo_sprinboot.entities.User;
import com.example.demo_sprinboot.exceptions.UserNotFoundException;
import com.example.demo_sprinboot.mappers.UserMapper;
import com.example.demo_sprinboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = UserMapper.INSTANCE; // Utilisation du UserMapper

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists.");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(encodedPassword);
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

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        // Mapper l'utilisateur en UserDTO sans le mot de passe
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return Optional.of(userDTO);
    }

    @Override
    public void logoutUser(UserDTO userDTO) {
        // ------------------------------------------------
        // -------------------------------------------------
    }

    @Override
    public Optional<UserDTO> getUserById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = userMapper.userToUserDTO(user);
            return Optional.of(userDTO);
        }
        return Optional.empty();
    }

}
