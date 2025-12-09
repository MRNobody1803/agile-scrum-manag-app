package com.example.agile.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "sprint_backlogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintBacklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserStory> userStories;

    @OneToOne(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    private Sprint sprint;

}