package com.example.demo_sprinboot.DTO;

import lombok.Data;
@Data
public class UserStoryDTO {
    private Long id;
    private String name;
    private String description;
    private Integer priority;
}