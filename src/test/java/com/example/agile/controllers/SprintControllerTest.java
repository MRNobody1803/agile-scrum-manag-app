package com.example.agile.controllers;

import com.example.agile.DTO.SprintDTO;
import com.example.agile.entities.Status;
import com.example.agile.exceptions.GlobalExceptionHandler;
import com.example.agile.services.SprintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SprintControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SprintService sprintService;

    @InjectMocks
    private SprintController sprintController;

    private ObjectMapper objectMapper;
    private SprintDTO sprintDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sprintController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sprintDTO = new SprintDTO();
        sprintDTO.setId(1L);
        sprintDTO.setName("Sprint 1");
        sprintDTO.setStartDate(LocalDate.of(2024, 1, 1));
        sprintDTO.setEndDate(LocalDate.of(2024, 1, 15));
        sprintDTO.setStatus(Status.TODO);
    }

    @Test
    void getAllSprints_ShouldReturnListOfSprints() throws Exception {
        // Given
        SprintDTO sprintDTO2 = new SprintDTO();
        sprintDTO2.setId(2L);
        sprintDTO2.setName("Sprint 2");
        sprintDTO2.setStartDate(LocalDate.of(2024, 1, 16));
        sprintDTO2.setEndDate(LocalDate.of(2024, 1, 30));
        sprintDTO2.setStatus(Status.IN_PROGRESS);

        List<SprintDTO> sprints = Arrays.asList(sprintDTO, sprintDTO2);
        when(sprintService.getAllSprints()).thenReturn(sprints);

        // When & Then
        mockMvc.perform(get("/api/sprints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Sprint 1")))
                .andExpect(jsonPath("$[0].status", is("TODO")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Sprint 2")))
                .andExpect(jsonPath("$[1].status", is("IN_PROGRESS")));

        verify(sprintService, times(1)).getAllSprints();
    }

    @Test
    void getSprintById_ShouldReturnSprint() throws Exception {
        // Given
        Long sprintId = 1L;
        when(sprintService.getSprintById(sprintId)).thenReturn(Optional.of(sprintDTO));

        // When & Then
        mockMvc.perform(get("/api/sprints/{id}", sprintId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Sprint 1")))
                .andExpect(jsonPath("$.startDate", is("2024-01-01")))
                .andExpect(jsonPath("$.endDate", is("2024-01-15")))
                .andExpect(jsonPath("$.status", is("TODO")));

        verify(sprintService, times(1)).getSprintById(sprintId);
    }

    @Test
    void getSprintById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long sprintId = 999L;
        when(sprintService.getSprintById(sprintId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/sprints/{id}", sprintId))
                .andExpect(status().isNotFound());

        verify(sprintService, times(1)).getSprintById(sprintId);
    }

    @Test
    void createSprint_ShouldReturnCreatedSprint() throws Exception {
        // Given
        when(sprintService.createSprint(any(SprintDTO.class))).thenReturn(sprintDTO);

        String jsonRequest = """
                {
                    "name": "Sprint 1",
                    "startDate": "2024-01-01",
                    "endDate": "2024-01-15",
                    "status": "TODO"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/sprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Sprint 1")))
                .andExpect(jsonPath("$.status", is("TODO")));

        verify(sprintService, times(1)).createSprint(any(SprintDTO.class));
    }

    @Test
    void updateSprint_ShouldReturnUpdatedSprint() throws Exception {
        // Given
        Long sprintId = 1L;
        SprintDTO updatedDTO = new SprintDTO();
        updatedDTO.setId(sprintId);
        updatedDTO.setName("Updated Sprint");
        updatedDTO.setStartDate(LocalDate.of(2024, 1, 1));
        updatedDTO.setEndDate(LocalDate.of(2024, 1, 15));
        updatedDTO.setStatus(Status.IN_PROGRESS);

        when(sprintService.updateSprint(eq(sprintId), any(SprintDTO.class))).thenReturn(updatedDTO);

        String jsonRequest = """
                {
                    "name": "Updated Sprint",
                    "startDate": "2024-01-01",
                    "endDate": "2024-01-15",
                    "status": "IN_PROGRESS"
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/sprints/{id}", sprintId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Sprint")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));

        verify(sprintService, times(1)).updateSprint(eq(sprintId), any(SprintDTO.class));
    }

    @Test
    void updateSprint_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long sprintId = 999L;
        when(sprintService.updateSprint(eq(sprintId), any(SprintDTO.class)))
                .thenThrow(new RuntimeException("Sprint not found"));

        String jsonRequest = """
                {
                    "name": "Sprint",
                    "startDate": "2024-01-01",
                    "endDate": "2024-01-15",
                    "status": "TODO"
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/sprints/{id}", sprintId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());

        verify(sprintService, times(1)).updateSprint(eq(sprintId), any(SprintDTO.class));
    }

    @Test
    void deleteSprint_ShouldReturnNoContent() throws Exception {
        // Given
        Long sprintId = 1L;
        doNothing().when(sprintService).deleteSprint(sprintId);

        // When & Then
        mockMvc.perform(delete("/api/sprints/{id}", sprintId))
                .andExpect(status().isNoContent());

        verify(sprintService, times(1)).deleteSprint(sprintId);
    }

    @Test
    void deleteSprint_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long sprintId = 999L;
        doThrow(new RuntimeException("Sprint not found")).when(sprintService).deleteSprint(sprintId);

        // When & Then
        mockMvc.perform(delete("/api/sprints/{id}", sprintId))
                .andExpect(status().isNotFound());

        verify(sprintService, times(1)).deleteSprint(sprintId);
    }

    @Test
    void startSprint_ShouldReturnOk() throws Exception {
        // Given
        Long sprintId = 1L;
        doNothing().when(sprintService).startSprint(sprintId);

        // When & Then
        mockMvc.perform(put("/api/sprints/{id}/start", sprintId))
                .andExpect(status().isOk());

        verify(sprintService, times(1)).startSprint(sprintId);
    }

    @Test
    void startSprint_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long sprintId = 999L;
        doThrow(new RuntimeException("Sprint not found")).when(sprintService).startSprint(sprintId);

        // When & Then
        mockMvc.perform(put("/api/sprints/{id}/start", sprintId))
                .andExpect(status().isNotFound());

        verify(sprintService, times(1)).startSprint(sprintId);
    }

    @Test
    void completeSprint_ShouldReturnOk() throws Exception {
        // Given
        Long sprintId = 1L;
        doNothing().when(sprintService).completeSprint(sprintId);

        // When & Then
        mockMvc.perform(put("/api/sprints/{id}/complete", sprintId))
                .andExpect(status().isOk());

        verify(sprintService, times(1)).completeSprint(sprintId);
    }

    @Test
    void completeSprint_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long sprintId = 999L;
        doThrow(new RuntimeException("Sprint not found")).when(sprintService).completeSprint(sprintId);

        // When & Then
        mockMvc.perform(put("/api/sprints/{id}/complete", sprintId))
                .andExpect(status().isNotFound());

        verify(sprintService, times(1)).completeSprint(sprintId);
    }

    @Test
    void createSprint_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        when(sprintService.createSprint(any(SprintDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid sprint data"));

        String jsonRequest = """
                {
                    "name": "",
                    "startDate": "2024-01-01",
                    "endDate": "2024-01-15",
                    "status": "TODO"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/sprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(sprintService, times(1)).createSprint(any(SprintDTO.class));
    }
}