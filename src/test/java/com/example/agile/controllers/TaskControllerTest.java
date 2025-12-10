package com.example.agile.controllers;

import com.example.agile.dto.TaskDTO;
import com.example.agile.entities.Status;
import com.example.agile.exceptions.GlobalExceptionHandler;
import com.example.agile.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        // Initialisation d'un TaskDTO pour les tests
        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setName("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus(Status.TODO);
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        // Given
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        // When & Then
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Task")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.status", is("TODO")));

        verify(taskService, times(1)).createTask(any(TaskDTO.class));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        // Given
        Long taskId = 1L;
        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setId(taskId);
        updatedTaskDTO.setName("Updated Task");
        updatedTaskDTO.setDescription("Updated Description");
        updatedTaskDTO.setStatus(Status.IN_PROGRESS);

        when(taskService.updateTask(eq(taskId), any(TaskDTO.class))).thenReturn(updatedTaskDTO);

        // When & Then
        mockMvc.perform(put("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Task")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));

        verify(taskService, times(1)).updateTask(eq(taskId), any(TaskDTO.class));
    }

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        // Given
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        // When & Then
        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    void getTaskById_ShouldReturnTask() throws Exception {
        // Given
        Long taskId = 1L;
        when(taskService.getTaskById(taskId)).thenReturn(taskDTO);

        // When & Then
        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Task")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.status", is("TODO")));

        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() throws Exception {
        // Given
        TaskDTO taskDTO2 = new TaskDTO();
        taskDTO2.setId(2L);
        taskDTO2.setName("Second Task");
        taskDTO2.setDescription("Second Description");
        taskDTO2.setStatus(Status.DONE);

        List<TaskDTO> taskList = Arrays.asList(taskDTO, taskDTO2);
        when(taskService.getAllTasks()).thenReturn(taskList);

        // When & Then
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Task")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Second Task")));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void createTask_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        when(taskService.createTask(any(TaskDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid task data"));

        // When & Then
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid task data"));

        verify(taskService, times(1)).createTask(any(TaskDTO.class));
    }

    @Test
    void updateTask_WithNonExistentId_ShouldReturnInternalServerError() throws Exception {
        // Given
        Long nonExistentId = 999L;
        when(taskService.updateTask(eq(nonExistentId), any(TaskDTO.class)))
                .thenThrow(new RuntimeException("Task not found"));

        // When & Then
        mockMvc.perform(put("/api/tasks/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Task not found"));

        verify(taskService, times(1)).updateTask(eq(nonExistentId), any(TaskDTO.class));
    }
}