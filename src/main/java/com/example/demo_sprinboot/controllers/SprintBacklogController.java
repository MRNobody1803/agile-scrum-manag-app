package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.DTO.SprintBacklogDTO;
import com.example.demo_sprinboot.services.SprintBacklogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sprint-backlogs")
public class SprintBacklogController {

    private final SprintBacklogService sprintBacklogService;

    public SprintBacklogController(SprintBacklogService sprintBacklogService) {
        this.sprintBacklogService = sprintBacklogService;
    }

    @GetMapping
    public ResponseEntity<List<SprintBacklogDTO>> getAllSprintBacklogs() {
        return ResponseEntity.ok(sprintBacklogService.getAllSprintBacklogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<SprintBacklogDTO>> getSprintBacklogById(@PathVariable Long id) {
        return ResponseEntity.ok(sprintBacklogService.getSprintBacklogById(id));
    }

    @PostMapping
    public ResponseEntity<SprintBacklogDTO> createSprintBacklog(@RequestBody SprintBacklogDTO sprintBacklogDTO) {
        SprintBacklogDTO createdSprintBacklog = sprintBacklogService.createSprintBacklog(sprintBacklogDTO);
        return ResponseEntity.status(201).body(createdSprintBacklog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintBacklogDTO> updateSprintBacklog(@PathVariable Long id, @RequestBody SprintBacklogDTO sprintBacklogDTO) {
        SprintBacklogDTO updatedSprintBacklog = sprintBacklogService.updateSprintBacklog(id, sprintBacklogDTO);
        return ResponseEntity.ok(updatedSprintBacklog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprintBacklog(@PathVariable Long id) {
        sprintBacklogService.deleteSprintBacklog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/name")
    public void updateSprintBacklogName(@PathVariable Long id, @RequestParam String newName) {
        sprintBacklogService.updateSprintBacklogName(id, newName);
    }

    @GetMapping("/search")
    public List<SprintBacklogDTO> searchSprintBacklogs(@RequestParam String keyword) {
        return sprintBacklogService.searchSprintBacklogByName(keyword);
    }

}
