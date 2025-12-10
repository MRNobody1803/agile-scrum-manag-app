package com.example.agile.controllers;

import com.example.agile.dto.AcceptanceCriteriaDTO;
import com.example.agile.entities.AcceptanceCriteria;
import com.example.agile.exceptions.GlobalExceptionHandler;
import com.example.agile.services.AcceptanceCriteriaService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AcceptanceCriteriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AcceptanceCriteriaService acceptanceCriteriaService;

    @InjectMocks
    private AcceptanceCriteriaController acceptanceCriteriaController;

    private ObjectMapper objectMapper;
    private AcceptanceCriteriaDTO criteriaDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(acceptanceCriteriaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        // Configuration critique pour ignorer les erreurs de désérialisation
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Initialisation d'un AcceptanceCriteriaDTO pour les tests
        criteriaDTO = new AcceptanceCriteriaDTO();
        criteriaDTO.setId(1L);
        criteriaDTO.setDescription("Test criteria description");
    }

    @Test
    void getAllAcceptanceCriteria_ShouldReturnListOfCriteria() throws Exception {
        // Given
        AcceptanceCriteriaDTO criteriaDTO2 = new AcceptanceCriteriaDTO();
        criteriaDTO2.setId(2L);
        criteriaDTO2.setDescription("Second criteria");

        List<AcceptanceCriteriaDTO> criteriaList = Arrays.asList(criteriaDTO, criteriaDTO2);
        when(acceptanceCriteriaService.getAllAcceptanceCriterias()).thenReturn(criteriaList);

        // When & Then
        mockMvc.perform(get("/api/acceptance-criteria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Test criteria description")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("Second criteria")));

        verify(acceptanceCriteriaService, times(1)).getAllAcceptanceCriterias();
    }

    @Test
    void getAcceptanceCriteriaById_ShouldReturnCriteria() throws Exception {
        // Given
        Long criteriaId = 1L;
        when(acceptanceCriteriaService.getAcceptanceCriteriaById(criteriaId))
                .thenReturn(Optional.of(criteriaDTO));

        // When & Then
        mockMvc.perform(get("/api/acceptance-criteria/{id}", criteriaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Test criteria description")));

        verify(acceptanceCriteriaService, times(1)).getAcceptanceCriteriaById(criteriaId);
    }

    @Test
    void getAcceptanceCriteriaById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long criteriaId = 999L;
        when(acceptanceCriteriaService.getAcceptanceCriteriaById(criteriaId))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/acceptance-criteria/{id}", criteriaId))
                .andExpect(status().isNotFound());

        verify(acceptanceCriteriaService, times(1)).getAcceptanceCriteriaById(criteriaId);
    }

    @Test
    void createAcceptanceCriteria_ShouldReturnCreatedCriteria() throws Exception {
        // Given - JSON minimal sans userStory
        String jsonRequest = """
                {
                    "description": "Test criteria description",
                    "isValid": false
                }
                """;

        when(acceptanceCriteriaService.createAcceptanceCriteria(any(AcceptanceCriteria.class)))
                .thenReturn(criteriaDTO);

        // When & Then
        mockMvc.perform(post("/api/acceptance-criteria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Test criteria description")));

        verify(acceptanceCriteriaService, times(1))
                .createAcceptanceCriteria(any(AcceptanceCriteria.class));
    }

    @Test
    void updateAcceptanceCriteria_ShouldReturnUpdatedCriteria() throws Exception {
        // Given
        Long criteriaId = 1L;
        String jsonRequest = """
                {
                    "description": "Updated criteria description",
                    "isValid": true
                }
                """;

        AcceptanceCriteriaDTO updatedDTO = new AcceptanceCriteriaDTO();
        updatedDTO.setId(criteriaId);
        updatedDTO.setDescription("Updated criteria description");

        when(acceptanceCriteriaService.updateAcceptanceCriteria(eq(criteriaId), any(AcceptanceCriteria.class)))
                .thenReturn(updatedDTO);

        // When & Then
        mockMvc.perform(put("/api/acceptance-criteria/{id}", criteriaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Updated criteria description")));

        verify(acceptanceCriteriaService, times(1))
                .updateAcceptanceCriteria(eq(criteriaId), any(AcceptanceCriteria.class));
    }

    @Test
    void updateAcceptanceCriteria_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long criteriaId = 999L;
        String jsonRequest = """
                {
                    "description": "Test description",
                    "isValid": false
                }
                """;

        when(acceptanceCriteriaService.updateAcceptanceCriteria(eq(criteriaId), any(AcceptanceCriteria.class)))
                .thenReturn(null);

        // When & Then
        mockMvc.perform(put("/api/acceptance-criteria/{id}", criteriaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());

        verify(acceptanceCriteriaService, times(1))
                .updateAcceptanceCriteria(eq(criteriaId), any(AcceptanceCriteria.class));
    }

    @Test
    void deleteAcceptanceCriteria_ShouldReturnNoContent() throws Exception {
        // Given
        Long criteriaId = 1L;
        when(acceptanceCriteriaService.deleteAcceptanceCriteria(criteriaId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/acceptance-criteria/{id}", criteriaId))
                .andExpect(status().isNoContent());

        verify(acceptanceCriteriaService, times(1)).deleteAcceptanceCriteria(criteriaId);
    }

    @Test
    void deleteAcceptanceCriteria_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        Long criteriaId = 999L;
        when(acceptanceCriteriaService.deleteAcceptanceCriteria(criteriaId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/acceptance-criteria/{id}", criteriaId))
                .andExpect(status().isNotFound());

        verify(acceptanceCriteriaService, times(1)).deleteAcceptanceCriteria(criteriaId);
    }

    @Test
    void createAcceptanceCriteria_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        String jsonRequest = """
                {
                    "description": "Test description",
                    "isValid": false
                }
                """;

        when(acceptanceCriteriaService.createAcceptanceCriteria(any(AcceptanceCriteria.class)))
                .thenThrow(new IllegalArgumentException("Invalid criteria data"));

        // When & Then
        mockMvc.perform(post("/api/acceptance-criteria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid criteria data"));

        verify(acceptanceCriteriaService, times(1))
                .createAcceptanceCriteria(any(AcceptanceCriteria.class));
    }
}