package com.example.demo_sprinboot.entities;

import com.example.demo_sprinboot.entities.Epic;
import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.entities.SprintBacklog;
import com.example.demo_sprinboot.entities.Status;
import com.example.demo_sprinboot.entities.Task;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user_stories")
@Data
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "sprint_backlog_id") // Changed to singular form
    private SprintBacklog sprintBacklog;

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id") // Changed to singular form
    private ProductBacklog productBacklog;

    public UserStory(String title) {
        this.title = title;
    }

    public UserStory(Long id, String title, ProductBacklog productBacklog) {
        this.id = id;
        this.title = title;
        this.productBacklog = productBacklog;
    }
}