package com.example.demo_sprinboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user_stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String asA;
    private String iWant;
    private String soThat;
    private int priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "epic_id") // Changed to singular form
    private Epic epic;

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id") // Changed to singular form
    private ProductBacklog productBacklog;


}