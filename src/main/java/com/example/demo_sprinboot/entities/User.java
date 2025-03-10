package com.example.demo_sprinboot.entities;

import com.example.demo_sprinboot.entities.RoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    public void register() {
        // registration logic
    }

    public void login() {
        // login logic
    }

    public void logout() {
        // logout logic
    }
}