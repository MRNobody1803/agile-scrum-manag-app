package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.entities.Task;
import com.example.demo_sprinboot.entities.UserStory;
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

    @GetMapping
    public ResponseEntity<List<UserStory>> getAllUserStories() {
        List<UserStory> userStories = userStoryService.getAllUserStories();
        return ResponseEntity.ok().body(userStories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStory> getUserStoryById(@PathVariable Long id) {
        UserStory userStory = userStoryService.getUserStoryById(id);
        return ResponseEntity.ok().body(userStory);
    }

    @PostMapping
    public ResponseEntity<UserStory> createUserStory(@RequestBody UserStory userStory) {
        UserStory createdUserStory = userStoryService.createUserStory(userStory);
        return ResponseEntity.status(201).body(createdUserStory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStory> updateUserStory(@PathVariable Long id, @RequestBody UserStory userStory) {
        UserStory updatedUserStory = userStoryService.updateUserStory(id, userStory);
        return ResponseEntity.ok().body(updatedUserStory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStoryById(@PathVariable Long id) {
        userStoryService.deleteUserStoryById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<UserStory> addTaskToUserStory(@PathVariable Long id, @RequestBody Task task) {
        UserStory userStory = userStoryService.addTaskToUserStory(id, task);
        return ResponseEntity.ok().body(userStory);
    }

    @PatchMapping("/{id}/asa-iwant-sothat")
    public ResponseEntity<UserStory> updateAsAAndIWantAndSoThat(@PathVariable Long id, @RequestParam String asA, @RequestParam String iWant, @RequestParam String soThat) {
        UserStory userStory = userStoryService.updateAsAAndIWantAndSoThat(id, asA, iWant, soThat);
        return ResponseEntity.ok().body(userStory);
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<UserStory> updatePriority(@PathVariable Long id, @RequestParam int priority) {
        UserStory userStory = userStoryService.updatePriority(id, priority);
        return ResponseEntity.ok().body(userStory);
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<UserStory> updateTitle(@PathVariable Long id, @RequestParam String title) {
        UserStory userStory = userStoryService.updateTitle(id, title);
        return ResponseEntity.ok().body(userStory);
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<UserStory> updateDescription(@PathVariable Long id, @RequestParam String description) {
        UserStory userStory = userStoryService.updateDescription(id, description);
        return ResponseEntity.ok().body(userStory);
    }
}