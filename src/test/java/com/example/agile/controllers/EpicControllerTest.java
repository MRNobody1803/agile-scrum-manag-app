package com.example.agile.controllers;

import com.example.agile.dto.EpicDTO;
import com.example.agile.exceptions.GlobalExceptionHandler;
import com.example.agile.services.EpicService;
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
class EpicControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EpicService epicService;

    @InjectMocks
    private EpicController epicController;

    private ObjectMapper objectMapper;
    private EpicDTO epicDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(epicController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        epicDTO = new EpicDTO();
        epicDTO.setId(1L);
        epicDTO.setName("Epic 1");
        epicDTO.setDescription("Epic Description");
    }

    @Test
    void getAllEpics_ShouldReturnListOfEpics() throws Exception {
        // Given
        EpicDTO epicDTO2 = new EpicDTO();
        epicDTO2.setId(2L);
        epicDTO2.setName("Epic 2");
        epicDTO2.setDescription("Second Epic");

        List<EpicDTO> epics = Arrays.asList(epicDTO, epicDTO2);
        when(epicService.getAllEpics()).thenReturn(epics);

        // When & Then
        mockMvc.perform(get("/api/epics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Epic 1")))
                .andExpect(jsonPath("$[0].description", is("Epic Description")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Epic 2")));

        verify(epicService, times(1)).getAllEpics();
    }

    @Test
    void getEpicById_ShouldReturnEpic() throws Exception {
        // Given
        long epicId = 1L;
        when(epicService.getEpicById(epicId)).thenReturn(epicDTO);

        // When & Then
        mockMvc.perform(get("/api/epics/{id}", epicId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Epic 1")))
                .andExpect(jsonPath("$.description", is("Epic Description")));

        verify(epicService, times(1)).getEpicById(epicId);
    }

    @Test
    void getEpicById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        long epicId = 999L;
        when(epicService.getEpicById(epicId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/epics/{id}", epicId))
                .andExpect(status().isNotFound());

        verify(epicService, times(1)).getEpicById(epicId);
    }

    @Test
    void createEpic_ShouldReturnCreatedEpic() throws Exception {
        // Given
        when(epicService.createEpic(any(EpicDTO.class))).thenReturn(epicDTO);

        // When & Then
        mockMvc.perform(post("/api/epics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(epicDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Epic 1")))
                .andExpect(jsonPath("$.description", is("Epic Description")));

        verify(epicService, times(1)).createEpic(any(EpicDTO.class));
    }

    @Test
    void updateEpic_ShouldReturnUpdatedEpic() throws Exception {
        // Given
        long epicId = 1L;
        EpicDTO updatedDTO = new EpicDTO();
        updatedDTO.setId(epicId);
        updatedDTO.setName("Updated Epic");
        updatedDTO.setDescription("Updated Description");

        when(epicService.updateEpic(eq(epicId), any(EpicDTO.class))).thenReturn(updatedDTO);

        // When & Then
        mockMvc.perform(put("/api/epics/{id}", epicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(epicDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Epic")))
                .andExpect(jsonPath("$.description", is("Updated Description")));

        verify(epicService, times(1)).updateEpic(eq(epicId), any(EpicDTO.class));
    }

    @Test
    void updateEpic_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        long epicId = 999L;
        when(epicService.updateEpic(eq(epicId), any(EpicDTO.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/api/epics/{id}", epicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(epicDTO)))
                .andExpect(status().isNotFound());

        verify(epicService, times(1)).updateEpic(eq(epicId), any(EpicDTO.class));
    }

    @Test
    void deleteEpic_ShouldReturnNoContent() throws Exception {
        // Given
        long epicId = 1L;
        when(epicService.deleteEpic(epicId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/epics/{id}", epicId))
                .andExpect(status().isNoContent());

        verify(epicService, times(1)).deleteEpic(epicId);
    }

    @Test
    void deleteEpic_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        long epicId = 999L;
        when(epicService.deleteEpic(epicId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/epics/{id}", epicId))
                .andExpect(status().isNotFound());

        verify(epicService, times(1)).deleteEpic(epicId);
    }

    @Test
    void createEpic_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        when(epicService.createEpic(any(EpicDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid epic data"));

        // When & Then
        mockMvc.perform(post("/api/epics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(epicDTO)))
                .andExpect(status().isBadRequest());

        verify(epicService, times(1)).createEpic(any(EpicDTO.class));
    }

    @Test
    void updateEpic_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        long epicId = 1L;
        when(epicService.updateEpic(eq(epicId), any(EpicDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid epic data"));

        // When & Then
        mockMvc.perform(put("/api/epics/{id}", epicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(epicDTO)))
                .andExpect(status().isBadRequest());

        verify(epicService, times(1)).updateEpic(eq(epicId), any(EpicDTO.class));
    }
}