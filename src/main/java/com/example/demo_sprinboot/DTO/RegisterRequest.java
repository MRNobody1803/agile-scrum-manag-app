package com.example.demo_sprinboot.DTO;

import com.example.demo_sprinboot.entities.RoleType;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private RoleType role;
}