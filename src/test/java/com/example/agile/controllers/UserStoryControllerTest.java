package com.example.agile.controllers;

import com.example.agile.DTO.UserStoryDTO;
import com.example.agile.entities.Priority;
import com.example.agile.entities.Task;
import com.example.agile.exceptions.GlobalExceptionHandler;
import com.example.agile.mappers.UserStoryMapper;
import com.example.agile.services.UserStoryService;
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
class UserStoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserStoryService userStoryService;

    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private UserStoryController userStoryController;

    private ObjectMapper objectMapper;
    private UserStoryDTO userStoryDTO;
    private Task task;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userStoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        // Initialisation d'un UserStoryDTO pour les tests
        userStoryDTO = new UserStoryDTO();
        userStoryDTO.setId(1L);
        userStoryDTO.setTitle("Test User Story");
        userStoryDTO.setDescription("Test Description");
        userStoryDTO.setAsA("user");
        userStoryDTO.setIWant("to test");
        userStoryDTO.setSoThat("it works");
        userStoryDTO.setPriority(Priority.HIGH.ordinal());

        // Initialisation d'une Task pour les tests
        task = new Task();
        task.setId(1L);
        task.setName("Test Task");
    }

    @Test
    void getAllUserStories_ShouldReturnListOfUserStories() throws Exception {
        // Given
        UserStoryDTO userStoryDTO2 = new UserStoryDTO();
        userStoryDTO2.setId(2L);
        userStoryDTO2.setTitle("Second User Story");

        List<UserStoryDTO> userStories = Arrays.asList(userStoryDTO, userStoryDTO2);
        when(userStoryService.getAllUserStories()).thenReturn(userStories);

        // When & Then
        mockMvc.perform(get("/api/userStories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test User Story")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Second User Story")));

        verify(userStoryService, times(1)).getAllUserStories();
    }

    @Test
    void getUserStoryById_ShouldReturnUserStory() throws Exception {
        // Given
        Long userStoryId = 1L;
        when(userStoryService.getUserStoryById(userStoryId)).thenReturn(userStoryDTO);

        // When & Then
        mockMvc.perform(get("/api/userStories/{id}", userStoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test User Story")))
                .andExpect(jsonPath("$.description", is("Test Description")));

        verify(userStoryService, times(1)).getUserStoryById(userStoryId);
    }

    @Test
    void createUserStory_ShouldReturnCreatedUserStory() throws Exception {
        // Given
        when(userStoryService.createUserStory(any(UserStoryDTO.class))).thenReturn(userStoryDTO);

        // When & Then
        mockMvc.perform(post("/api/userStories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userStoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test User Story")));

        verify(userStoryService, times(1)).createUserStory(any(UserStoryDTO.class));
    }

    @Test
    void updateUserStory_ShouldReturnUpdatedUserStory() throws Exception {
        // Given
        Long userStoryId = 1L;
        UserStoryDTO updatedDTO = new UserStoryDTO();
        updatedDTO.setId(userStoryId);
        updatedDTO.setTitle("Updated User Story");

        when(userStoryService.updateUserStory(eq(userStoryId), any(UserStoryDTO.class)))
                .thenReturn(updatedDTO);

        // When & Then
        mockMvc.perform(put("/api/userStories/{id}", userStoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userStoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated User Story")));

        verify(userStoryService, times(1))
                .updateUserStory(eq(userStoryId), any(UserStoryDTO.class));
    }

    @Test
    void deleteUserStoryById_ShouldReturnNoContent() throws Exception {
        // Given
        Long userStoryId = 1L;
        doNothing().when(userStoryService).deleteUserStoryById(userStoryId);

        // When & Then
        mockMvc.perform(delete("/api/userStories/{id}", userStoryId))
                .andExpect(status().isNoContent());

        verify(userStoryService, times(1)).deleteUserStoryById(userStoryId);
    }

    @Test
    void addTaskToUserStory_ShouldReturnUserStoryWithTask() throws Exception {
        // Given
        Long userStoryId = 1L;
        when(userStoryService.addTaskToUserStory(eq(userStoryId), any(Task.class)))
                .thenReturn(userStoryDTO);

        // When & Then
        mockMvc.perform(post("/api/userStories/{id}/tasks", userStoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(userStoryService, times(1)).addTaskToUserStory(eq(userStoryId), any(Task.class));
    }

    @Test
    void updateAsAAndIWantAndSoThat_ShouldReturnUpdatedUserStory() throws Exception {
        // Given
        Long userStoryId = 1L;
        String asA = "developer";
        String iWant = "to write tests";
        String soThat = "code quality improves";

        when(userStoryService.updateAsAAndIWantAndSoThat(userStoryId, asA, iWant, soThat))
                .thenReturn(userStoryDTO);

        // When & Then
        mockMvc.perform(patch("/api/userStories/{id}/asa-iwant-sothat", userStoryId)
                        .param("asA", asA)
                        .param("iWant", iWant)
                        .param("soThat", soThat))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(userStoryService, times(1))
                .updateAsAAndIWantAndSoThat(userStoryId, asA, iWant, soThat);
    }

    @Test
    void updatePriority_ShouldReturnUpdatedUserStory() throws Exception {
        // Given
        Long userStoryId = 1L;
        String priority = "LOW";

        when(userStoryService.updatePriority(userStoryId, priority)).thenReturn(userStoryDTO);

        // When & Then
        mockMvc.perform(patch("/api/userStories/{id}/priority", userStoryId)
                        .param("priority", priority))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(userStoryService, times(1)).updatePriority(userStoryId, priority);
    }

    @Test
    void updateTitle_ShouldReturnUpdatedUserStory() throws Exception {
        // Given
        Long userStoryId = 1L;
        String title = "New Title";

        when(userStoryService.updateTitle(userStoryId, title)).thenReturn(userStoryDTO);

        // When & Then
        mockMvc.perform(patch("/api/userStories/{id}/title", userStoryId)
                        .param("title", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(userStoryService, times(1)).updateTitle(userStoryId, title);
    }

    @Test
    void updateDescription_ShouldReturnUpdatedUserStory() throws Exception {
        // Given
        Long userStoryId = 1L;
        String description = "New Description";

        when(userStoryService.updateDescription(userStoryId, description))
                .thenReturn(userStoryDTO);

        // When & Then
        mockMvc.perform(patch("/api/userStories/{id}/description", userStoryId)
                        .param("description", description))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(userStoryService, times(1)).updateDescription(userStoryId, description);
    }

    @Test
    void createUserStory_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        when(userStoryService.createUserStory(any(UserStoryDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid user story data"));

        // When & Then
        mockMvc.perform(post("/api/userStories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userStoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user story data"));

        verify(userStoryService, times(1)).createUserStory(any(UserStoryDTO.class));
    }

    @Test
    void getUserStoryById_WhenNotFound_ShouldReturnInternalServerError() throws Exception {
        // Given
        Long userStoryId = 999L;
        when(userStoryService.getUserStoryById(userStoryId))
                .thenThrow(new RuntimeException("User story not found"));

        // When & Then
        mockMvc.perform(get("/api/userStories/{id}", userStoryId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("User story not found"));

        verify(userStoryService, times(1)).getUserStoryById(userStoryId);
    }
}