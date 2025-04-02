package com.example.demo_sprinboot.DTO;

import com.example.demo_sprinboot.entities.Status; // Importer l'énum Status
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Status cannot be null")
    private Status status; // Utilise Status plutôt que String

//    @NotNull(message = "User story cannot be null")
    private Long userStoryId;
}
