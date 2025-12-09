package com.example.agile.DTO;

import com.example.agile.entities.Status;
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