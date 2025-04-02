package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.TaskDTO;
import com.example.demo_sprinboot.entities.Status;
import com.example.demo_sprinboot.entities.Task;
import com.example.demo_sprinboot.entities.UserStory;
import com.example.demo_sprinboot.mappers.TaskMapper;
import com.example.demo_sprinboot.repository.TaskRepository;
import com.example.demo_sprinboot.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserStoryRepository userStoryRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        // Retrieve the UserStory associated with the Task
        Optional<UserStory> userStory = userStoryRepository.findById(taskDTO.getUserStoryId());
        if (userStory.isEmpty()) {
            throw new IllegalArgumentException("UserStory not found with ID: " + taskDTO.getUserStoryId());
        }

        // Convert DTO to Entity
        Task task = taskMapper.toEntity(taskDTO);
        task.setUserStory(userStory.get()); // Set the associated UserStory

        // Save the Task
        Task savedTask = taskRepository.save(task);

        // Return the saved Task as a DTO
        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        // Retrieve the existing Task
        Optional<Task> existingTaskOptional = taskRepository.findById(id);
        if (existingTaskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found with ID: " + id);
        }

        // Retrieve the UserStory associated with the Task
        Optional<UserStory> userStory = userStoryRepository.findById(taskDTO.getUserStoryId());
        if (userStory.isEmpty()) {
            throw new IllegalArgumentException("UserStory not found with ID: " + taskDTO.getUserStoryId());
        }

        // Update the Task
        Task existingTask = existingTaskOptional.get();
        existingTask.setName(taskDTO.getName());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setUserStory(userStory.get());

        // Save the updated Task
        Task updatedTask = taskRepository.save(existingTask);

        // Return the updated Task as a DTO
        return taskMapper.toDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        // Check if the Task exists
        Optional<Task> existingTaskOptional = taskRepository.findById(id);
        if (existingTaskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found with ID: " + id);
        }

        // Delete the Task
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        // Retrieve the Task by ID
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found with ID: " + id);
        }

        // Return the Task as a DTO
        return taskMapper.toDto(taskOptional.get());
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        // Retrieve all tasks and map them to DTOs
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDto(tasks);
    }
}
