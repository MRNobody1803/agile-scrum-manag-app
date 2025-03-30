package com.example.demo_sprinboot.DTO;

import com.example.demo_sprinboot.entities.Priority;
import com.example.demo_sprinboot.entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryDTO {
    private Long id;


    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;


    private String description;


    private String asA;


    private String iWant;


    private String soThat;


    private int priority;


    private Status status;

    private Long epicId;
    private Long sprintBacklogId;
}