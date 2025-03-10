package com.example.demo_sprinboot.entities;


import com.example.demo_sprinboot.entities.SprintBacklog;
import com.example.demo_sprinboot.entities.UserStory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_story_id")
    private UserStory userStory;

    @ManyToOne
    @JoinColumn(name = "sprint_backlog_id")
    private SprintBacklog sprintBacklog;
}
