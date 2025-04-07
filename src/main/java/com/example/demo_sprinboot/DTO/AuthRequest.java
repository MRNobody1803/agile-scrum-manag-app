package com.example.demo_sprinboot.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}