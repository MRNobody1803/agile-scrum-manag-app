package com.example.demo_sprinboot.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "sprint_backlogs")
@Data
@NoArgsConstructor
public class SprintBacklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserStory> userStories;

    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToOne(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    private Sprint sprint;
}