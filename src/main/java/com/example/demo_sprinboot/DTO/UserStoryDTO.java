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

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "As a cannot be blank")
    private String asA;

    @NotBlank(message = "I want cannot be blank")
    private String iWant;

    @NotBlank(message = "So that cannot be blank")
    private String soThat;

    @NotNull(message = "Priority cannot be null")
    private Priority priority;

    @NotNull(message = "Status cannot be null")
    private Status status;

    private Long epicId;
    private Long productBacklogId;
    private Long sprintBacklogId;
}