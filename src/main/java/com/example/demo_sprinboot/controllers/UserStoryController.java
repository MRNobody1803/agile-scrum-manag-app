package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.DTO.UserStoryDTO;
import com.example.demo_sprinboot.entities.Task;
import com.example.demo_sprinboot.mappers.UserStoryMapper;
import com.example.demo_sprinboot.services.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userStories")
public class UserStoryController {

    @Autowired
    private UserStoryService userStoryService;

    @Autowired
    private UserStoryMapper userStoryMapper;

    @GetMapping
    public ResponseEntity<List<UserStoryDTO>> getAllUserStories() {
        List<UserStoryDTO> userStories = userStoryService.getAllUserStories();
        return ResponseEntity.ok().body(userStories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStoryDTO> getUserStoryById(@PathVariable Long id) {
        UserStoryDTO userStory = userStoryService.getUserStoryById(id);
        return ResponseEntity.ok().body(userStory);
    }

    @PostMapping
    public ResponseEntity<UserStoryDTO> createUserStory(@RequestBody UserStoryDTO userStoryDTO) {
        UserStoryDTO createdUserStory = userStoryService.createUserStory(userStoryDTO);
        return ResponseEntity.status(201).body(createdUserStory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> updateUserStory(@PathVariable Long id, @RequestBody UserStoryDTO userStoryDTO) {
        UserStoryDTO updatedUserStory = userStoryService.updateUserStory(id, userStoryDTO);
        return ResponseEntity.ok().body(updatedUserStory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStoryById(@PathVariable Long id) {
        userStoryService.deleteUserStoryById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<UserStoryDTO> addTaskToUserStory(@PathVariable Long id, @RequestBody Task task) {
        UserStoryDTO userStory = userStoryService.addTaskToUserStory(id, task);
        return ResponseEntity.ok().body(userStory);
    }

    @PatchMapping("/{id}/asa-iwant-sothat")
    public ResponseEntity<UserStoryDTO> updateAsAAndIWantAndSoThat(
            @PathVariable Long id,
            @RequestParam String asA,
            @RequestParam String iWant,
            @RequestParam String soThat) {
        UserStoryDTO userStory = userStoryService.updateAsAAndIWantAndSoThat(id, asA, iWant, soThat);
        return ResponseEntity.ok().body(userStory);
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<UserStoryDTO> updatePriority(@PathVariable Long id, @RequestParam String priority) {
        UserStoryDTO userStory = userStoryService.updatePriority(id, priority);
        return ResponseEntity.ok().body(userStory);
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<UserStoryDTO> updateTitle(@PathVariable Long id, @RequestParam String title) {
        UserStoryDTO userStory = userStoryService.updateTitle(id, title);
        return ResponseEntity.ok().body(userStory);
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<UserStoryDTO> updateDescription(@PathVariable Long id, @RequestParam String description) {
        UserStoryDTO userStory = userStoryService.updateDescription(id, description);
        return ResponseEntity.ok().body(userStory);
    }
}
