package com.example.demo_sprinboot.services;


import com.example.demo_sprinboot.entities.Task;
import com.example.demo_sprinboot.entities.UserStory;
import com.example.demo_sprinboot.mappers.UserStoryMapper;
import com.example.demo_sprinboot.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStoryServiceImpl implements UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final UserStoryMapper userStoryMapper = UserStoryMapper.INSTANCE;

    @Autowired
    public UserStoryServiceImpl(UserStoryRepository userStoryRepository) {
        this.userStoryRepository = userStoryRepository;
    }

    @Override
    public List<UserStory> getAllUserStories() {
        return userStoryRepository.findAll();
    }

    @Override
    public UserStory getUserStoryById(Long id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User story with id " + id + " not found"));
    }

    @Override
    public UserStory createUserStory(UserStory userStory) {
        return userStoryRepository.save(userStory);
    }

    @Override
    public UserStory updateUserStory(Long id, UserStory userStory) {
        Optional<UserStory> existingUserStory = userStoryRepository.findById(id);
        if (existingUserStory.isPresent()) {
            UserStory updatedUserStory = existingUserStory.get();
            updatedUserStory.setTitle(userStory.getTitle());
            updatedUserStory.setDescription(userStory.getDescription());
            updatedUserStory.setAsA(userStory.getAsA());
            updatedUserStory.setIWant(userStory.getIWant());
            updatedUserStory.setSoThat(userStory.getSoThat());
            updatedUserStory.setPriority(userStory.getPriority());
            updatedUserStory.setStatus(userStory.getStatus());
            updatedUserStory.setEpic(userStory.getEpic());
            updatedUserStory.setSprintBacklog(userStory.getSprintBacklog());
            updatedUserStory.setTasks(userStory.getTasks());
            updatedUserStory.setProductBacklog(userStory.getProductBacklog());
            return userStoryRepository.save(updatedUserStory);
        } else {
            throw new RuntimeException("User story with id " + id + " not found");
        }
    }

    @Override
    public void deleteUserStoryById(Long id) {
        if (userStoryRepository.existsById(id)) {
            userStoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("User story with id " + id + " not found");
        }
    }

    @Override
    public UserStory addTaskToUserStory(Long userStoryId, Task task) {
        UserStory userStory = getUserStoryById(userStoryId);
        userStory.getTasks().add(task);
        task.setUserStory(userStory);
        return userStoryRepository.save(userStory);
    }

    @Override
    public UserStory updateAsAAndIWantAndSoThat(Long id, String asA, String iWant, String soThat) {
        UserStory userStory = getUserStoryById(id);
        userStory.setAsA(asA);
        userStory.setIWant(iWant);
        userStory.setSoThat(soThat);
        return userStoryRepository.save(userStory);
    }

    @Override
    public UserStory updatePriority(Long id, int priority) {
        UserStory userStory = getUserStoryById(id);
        userStory.setPriority(priority);
        return userStoryRepository.save(userStory);
    }

    @Override
    public UserStory updateTitle(Long id, String title) {
        UserStory userStory = getUserStoryById(id);
        userStory.setTitle(title);
        return userStoryRepository.save(userStory);
    }

    @Override
    public UserStory updateDescription(Long id, String description) {
        UserStory userStory = getUserStoryById(id);
        userStory.setDescription(description);
        return userStoryRepository.save(userStory);
    }
}