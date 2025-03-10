package com.example.demo_sprinboot.services;


import com.example.demo_sprinboot.entities.Task;
import com.example.demo_sprinboot.entities.UserStory;

import java.util.List;

public interface UserStoryService {
    List<UserStory> getAllUserStories();
    UserStory getUserStoryById(Long id);
    UserStory createUserStory(UserStory userStory);
    UserStory updateUserStory(Long id, UserStory userStory);
    void deleteUserStoryById(Long id);
    UserStory addTaskToUserStory(Long userStoryId, Task task);
    UserStory updateAsAAndIWantAndSoThat(Long id, String asA, String iWant, String soThat);
    UserStory updatePriority(Long id, int priority);
    UserStory updateTitle(Long id, String title);
    UserStory updateDescription(Long id, String description);
}