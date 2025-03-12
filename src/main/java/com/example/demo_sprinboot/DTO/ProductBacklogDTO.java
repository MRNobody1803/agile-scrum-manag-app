package com.example.demo_sprinboot.DTO;

import com.example.demo_sprinboot.entities.Priority;
import com.example.demo_sprinboot.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBacklogDTO {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Priority cannot be null")
    private Priority priority;

    @NotNull(message = "Status cannot be null")
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> userStoryIds;
}