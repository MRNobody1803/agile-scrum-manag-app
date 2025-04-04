package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.TaskDTO;
import com.example.demo_sprinboot.entities.Status;
import com.example.demo_sprinboot.entities.Task;
import com.example.demo_sprinboot.entities.UserStory;
import com.example.demo_sprinboot.mappers.TaskMapper;
import com.example.demo_sprinboot.repository.TaskRepository;
import com.example.demo_sprinboot.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDTO taskDTO;
    private UserStory userStory;

    @BeforeEach
    void setUp() {
        userStory = new UserStory();
        userStory.setId(1L);

        task = new Task();
        task.setId(1L);
        task.setName("Task 1");
        task.setDescription("Description 1");
        task.setStatus(Status.TODO);
        task.setUserStory(userStory);

        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setName("Task 1");
        taskDTO.setDescription("Description 1");
        taskDTO.setStatus(Status.TODO);
        taskDTO.setUserStoryId(1L);
    }

    @Test
    void testCreateTask_Success() {
        // Mock du repository et du mapper
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        // Appel de la méthode
        TaskDTO createdTask = taskService.createTask(taskDTO);

        // Vérifications
        assertNotNull(createdTask);
        assertEquals(taskDTO.getName(), createdTask.getName());
        assertEquals(taskDTO.getDescription(), createdTask.getDescription());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask_Success() {
        // Mock du repository et du mapper
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDTO);

        // Appel de la méthode
        TaskDTO updatedTask = taskService.updateTask(1L, taskDTO);

        // Vérifications
        assertNotNull(updatedTask);
        assertEquals(taskDTO.getName(), updatedTask.getName());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testDeleteTask_Success() {
        // Mock du repository
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(1L);

        // Appel de la méthode
        taskService.deleteTask(1L);

        // Vérifications
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetTaskById_Success() {
        // Mock du repository et du mapper
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        // Appel de la méthode
        TaskDTO retrievedTask = taskService.getTaskById(1L);

        // Vérifications
        assertNotNull(retrievedTask);
        assertEquals(taskDTO.getName(), retrievedTask.getName());
    }

    @Test
    void testGetAllTasks_Success() {
        // Mock du repository et du mapper
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        // Appel de la méthode
        List<TaskDTO> taskList = taskService.getAllTasks();

        // Vérifications
        assertNotNull(taskList);
        assertEquals(1, taskList.size());
        verify(taskRepository, times(1)).findAll();
    }
}
