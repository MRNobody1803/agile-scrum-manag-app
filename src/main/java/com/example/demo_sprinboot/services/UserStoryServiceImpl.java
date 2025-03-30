package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.UserStoryDTO;
import com.example.demo_sprinboot.entities.*;
import com.example.demo_sprinboot.exceptions.ResourceNotFoundException;
import com.example.demo_sprinboot.mappers.UserStoryMapper;
import com.example.demo_sprinboot.repository.EpicRepository;
import com.example.demo_sprinboot.repository.SprintBacklogRepository;
import com.example.demo_sprinboot.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserStoryServiceImpl implements UserStoryService {

    private final EpicRepository epicRepository;
    private final SprintBacklogRepository sprintBacklogRepository;
    private final UserStoryRepository userStoryRepository;
    private final UserStoryMapper userStoryMapper;

    @Autowired
    public UserStoryServiceImpl(EpicRepository epicRepository, SprintBacklogRepository sprintBacklogRepository, UserStoryRepository userStoryRepository, UserStoryMapper userStoryMapper) {
        this.epicRepository = epicRepository;
        this.sprintBacklogRepository = sprintBacklogRepository;
        this.userStoryRepository = userStoryRepository;
        this.userStoryMapper = userStoryMapper;
    }

    @Override
    @Cacheable("userStories")
    public List<UserStoryDTO> getAllUserStories() {
        return userStoryRepository.findAll().stream()
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "userStories", key = "#id")
    public UserStoryDTO getUserStoryById(Long id) {
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story with id " + id + " not found"));
        return userStoryMapper.toDto(userStory);
    }

    @Override
    @CacheEvict(value = "userStories", allEntries = true)
    public UserStoryDTO createUserStory(UserStoryDTO userStoryDTO) {
        UserStory userStory = userStoryMapper.toEntity(userStoryDTO);
        UserStory savedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(savedUserStory);
    }

    @Override
    @CacheEvict(value = "userStories", key = "#id")
    public UserStoryDTO updateUserStory(Long id, UserStoryDTO userStoryDTO) {
        UserStory existingUserStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story with id " + id + " not found"));

        existingUserStory.setTitle(userStoryDTO.getTitle());
        existingUserStory.setDescription(userStoryDTO.getDescription());
        existingUserStory.setAsA(userStoryDTO.getAsA());
        existingUserStory.setIWant(userStoryDTO.getIWant());
        existingUserStory.setSoThat(userStoryDTO.getSoThat());
        existingUserStory.setPriority(Priority.values()[userStoryDTO.getPriority()]);
        existingUserStory.setStatus(userStoryDTO.getStatus());

        if (userStoryDTO.getEpicId() != null) {
            Epic epic = epicRepository.findById(userStoryDTO.getEpicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Epic not found"));
            existingUserStory.setEpic(epic);
        }

        if (userStoryDTO.getSprintBacklogId() != null) {
            SprintBacklog sprintBacklog = sprintBacklogRepository.findById(userStoryDTO.getSprintBacklogId())
                    .orElseThrow(() -> new ResourceNotFoundException("SprintBacklog not found"));
            existingUserStory.setSprintBacklog(sprintBacklog);
        }

        UserStory updatedUserStory = userStoryRepository.save(existingUserStory);
        return userStoryMapper.toDto(updatedUserStory);
    }

    @Override
    @CacheEvict(value = "userStories", key = "#id")
    public void deleteUserStoryById(Long id) {
        if (!userStoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("User story with id " + id + " not found");
        }
        userStoryRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "userStories", key = "#userStoryId")
    public UserStoryDTO addTaskToUserStory(Long userStoryId, Task task) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new ResourceNotFoundException("User story with id " + userStoryId + " not found"));
        userStory.getTasks().add(task);
        task.setUserStory(userStory);
        UserStory updatedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(updatedUserStory);
    }

    @Override
    public UserStoryDTO updateAsAAndIWantAndSoThat(Long id, String asA, String iWant, String soThat) {
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story with id " + id + " not found"));

        userStory.setAsA(asA);
        userStory.setIWant(iWant);
        userStory.setSoThat(soThat);

        UserStory updatedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(updatedUserStory);
    }

    @Override
    @CacheEvict(value = "userStories", key = "#id")
    public UserStoryDTO updatePriority(Long id, String priority) {
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story with id " + id + " not found"));

        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            userStory.setPriority(priorityEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority value: " + priority);
        }

        UserStory updatedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(updatedUserStory);
    }

    @Override
    @CacheEvict(value = "userStories", key = "#id")
    public UserStoryDTO updateTitle(Long id, String title) {
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story with id " + id + " not found"));

        userStory.setTitle(title);
        UserStory updatedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(updatedUserStory);
    }

    @Override
    @CacheEvict(value = "userStories", key = "#id")
    public UserStoryDTO updateDescription(Long id, String description) {
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story with id " + id + " not found"));

        userStory.setDescription(description);
        UserStory updatedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(updatedUserStory);
    }



}
