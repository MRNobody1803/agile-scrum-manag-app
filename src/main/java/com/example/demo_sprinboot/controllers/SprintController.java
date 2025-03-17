package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.DTO.SprintDTO;
import com.example.demo_sprinboot.services.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sprints")
public class SprintController {

    private final SprintService sprintService;

    @Autowired
    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping
    @Cacheable(value = "sprints")
    public ResponseEntity<List<SprintDTO>> getAllSprints() {
        List<SprintDTO> sprints = sprintService.getAllSprints();
        return ResponseEntity.ok(sprints);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "sprints", key = "#id")
    public ResponseEntity<SprintDTO> getSprintById(@PathVariable Long id) {
        Optional<SprintDTO> sprint = sprintService.getSprintById(id);
        return sprint.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @CacheEvict(value = "sprints", allEntries = true) // Efface le cache car la liste change
    public ResponseEntity<SprintDTO> createSprint(@RequestBody SprintDTO sprintDTO) {
        SprintDTO createdSprint = sprintService.createSprint(sprintDTO);
        return ResponseEntity.ok(createdSprint);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "sprints", key = "#id") // Supprime du cache l'ancien sprint
    public ResponseEntity<SprintDTO> updateSprint(@PathVariable Long id, @RequestBody SprintDTO sprintDTO) {
        try {
            SprintDTO updatedSprint = sprintService.updateSprint(id, sprintDTO);
            return ResponseEntity.ok(updatedSprint);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "sprints", key = "#id") // Supprime du cache
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        try {
            sprintService.deleteSprint(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/start")
    @CacheEvict(value = "sprints", key = "#id")
    public ResponseEntity<Void> startSprint(@PathVariable Long id) {
        try {
            sprintService.startSprint(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/complete")
    @CacheEvict(value = "sprints", key = "#id")
    public ResponseEntity<Void> completeSprint(@PathVariable Long id) {
        try {
            sprintService.completeSprint(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
