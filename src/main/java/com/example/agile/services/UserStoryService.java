package com.example.agile.services;


import com.example.agile.DTO.UserStoryDTO;
import com.example.agile.entities.Task;

import java.util.List;

public interface UserStoryService {
    List<UserStoryDTO> getAllUserStories();
    UserStoryDTO getUserStoryById(Long id);
    UserStoryDTO createUserStory(UserStoryDTO userStory);
    UserStoryDTO updateUserStory(Long id, UserStoryDTO userStory);
    void deleteUserStoryById(Long id);
    UserStoryDTO addTaskToUserStory(Long userStoryId, Task task);
    UserStoryDTO updateAsAAndIWantAndSoThat(Long id, String asA, String iWant, String soThat);
    UserStoryDTO updatePriority(Long id, String priority);
    UserStoryDTO updateTitle(Long id, String title);
    UserStoryDTO updateDescription(Long id, String description);
}