package com.example.demo_sprinboot.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor @Accessors @Getter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "product_backlogs")
public class ProductBacklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                  // title

    private String description;

    private String priority ;              // HIGHT, MEDIUM, LOW

    @Enumerated (EnumType.STRING)
    private Status status ;                 // TO DO , IN PROGRESS , DONE

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt ;       // date of creation

    private LocalDateTime updatedAt ;

    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserStory> userStories;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
