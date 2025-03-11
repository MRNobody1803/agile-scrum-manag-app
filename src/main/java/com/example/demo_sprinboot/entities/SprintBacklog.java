package com.example.demo_sprinboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "sprint_backlogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintBacklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserStory> userStories;

    @OneToOne(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    private Sprint sprint;
}