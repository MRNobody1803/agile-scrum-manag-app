package com.example.demo_sprinboot.entities;

import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.entities.UserStory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Data
@AllArgsConstructor @Accessors @Getter
@NoArgsConstructor
@Builder
@Table(name = "epics")
public class Epic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JsonBackReference
    private ProductBacklog productBacklog;

    @OneToMany(mappedBy = "epic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UserStory> userStories;

}
