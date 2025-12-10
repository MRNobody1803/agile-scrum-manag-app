package com.example.agile.dto;

import com.example.agile.entities.RoleType;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private RoleType role;
}