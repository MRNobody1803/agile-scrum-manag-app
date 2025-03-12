package com.example.demo_sprinboot.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptanceCriteriaDTO {
    private Long id;

    @NotNull(message = "User story ID cannot be null")
    private Long userStoryId;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private LocalDateTime createdAt;
}