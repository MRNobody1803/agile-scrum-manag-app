package com.example.agile.controllers;

import com.example.agile.dto.ProductBacklogDTO;
import com.example.agile.entities.Priority;
import com.example.agile.entities.Status;
import com.example.agile.exceptions.GlobalExceptionHandler;
import com.example.agile.services.ProductBacklogService;
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

import java.time.LocalDateTime;
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
class ProductBacklogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductBacklogService productBacklogService;

    @InjectMocks
    private ProductBacklogController productBacklogController;

    private ObjectMapper objectMapper;
    private ProductBacklogDTO productBacklogDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productBacklogController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        productBacklogDTO = new ProductBacklogDTO();
        productBacklogDTO.setId(1L);
        productBacklogDTO.setName("Product Backlog 1");
        productBacklogDTO.setDescription("Description of backlog");
        productBacklogDTO.setPriority(Priority.HIGH);
        productBacklogDTO.setStatus(Status.TODO);
        productBacklogDTO.setCreatedAt(LocalDateTime.now());
        productBacklogDTO.setUpdatedAt(LocalDateTime.now());
        productBacklogDTO.setUserStoryIds(Arrays.asList(1L, 2L, 3L));
    }

    @Test
    void getAllProductBacklogs_ShouldReturnListOfBacklogs() throws Exception {
        // Given
        ProductBacklogDTO productBacklogDTO2 = new ProductBacklogDTO();
        productBacklogDTO2.setId(2L);
        productBacklogDTO2.setName("Product Backlog 2");
        productBacklogDTO2.setDescription("Second backlog");
        productBacklogDTO2.setPriority(Priority.MIDIUM);
        productBacklogDTO2.setStatus(Status.IN_PROGRESS);
        productBacklogDTO2.setUserStoryIds(Arrays.asList(4L, 5L));

        List<ProductBacklogDTO> backlogs = Arrays.asList(productBacklogDTO, productBacklogDTO2);
        when(productBacklogService.getAllProductBacklogs()).thenReturn(backlogs);

        // When & Then
        mockMvc.perform(get("/api/backlogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Product Backlog 1")))
                .andExpect(jsonPath("$[0].priority", is("HIGH")))
                .andExpect(jsonPath("$[0].status", is("TODO")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Product Backlog 2")))
                .andExpect(jsonPath("$[1].priority", is("MIDIUM")))
                .andExpect(jsonPath("$[1].status", is("IN_PROGRESS")));

        verify(productBacklogService, times(1)).getAllProductBacklogs();
    }

    @Test
    void getProductBacklogById_ShouldReturnBacklog() throws Exception {
        // Given
        long backlogId = 1L;
        when(productBacklogService.getProductBacklogById(backlogId))
                .thenReturn(productBacklogDTO);

        // When & Then
        mockMvc.perform(get("/api/backlogs/{id}", backlogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product Backlog 1")))
                .andExpect(jsonPath("$.description", is("Description of backlog")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.status", is("TODO")));

        verify(productBacklogService, times(1)).getProductBacklogById(backlogId);
    }

    @Test
    void getProductBacklogById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        long backlogId = 999L;
        when(productBacklogService.getProductBacklogById(backlogId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/backlogs/{id}", backlogId))
                .andExpect(status().isNotFound());

        verify(productBacklogService, times(1)).getProductBacklogById(backlogId);
    }

    @Test
    void createProductBacklog_ShouldReturnCreatedBacklog() throws Exception {
        // Given
        when(productBacklogService.createProductBacklog(any(ProductBacklogDTO.class)))
                .thenReturn(productBacklogDTO);

        String jsonRequest = """
                {
                    "name": "Product Backlog 1",
                    "description": "Description of backlog",
                    "priority": "HIGH",
                    "status": "TODO",
                    "userStoryIds": [1, 2, 3]
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/backlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product Backlog 1")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.status", is("TODO")));

        verify(productBacklogService, times(1))
                .createProductBacklog(any(ProductBacklogDTO.class));
    }

    @Test
    void updateProductBacklog_ShouldReturnUpdatedBacklog() throws Exception {
        // Given
        long backlogId = 1L;
        ProductBacklogDTO updatedDTO = new ProductBacklogDTO();
        updatedDTO.setId(backlogId);
        updatedDTO.setName("Updated Product Backlog");
        updatedDTO.setDescription("Updated description");
        updatedDTO.setPriority(Priority.LOW);
        updatedDTO.setStatus(Status.DONE);
        updatedDTO.setUserStoryIds(Arrays.asList(1L, 2L));

        when(productBacklogService.updateProductBacklog(eq(backlogId), any(ProductBacklogDTO.class)))
                .thenReturn(updatedDTO);

        String jsonRequest = """
                {
                    "name": "Updated Product Backlog",
                    "description": "Updated description",
                    "priority": "LOW",
                    "status": "DONE",
                    "userStoryIds": [1, 2]
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/backlogs/{id}", backlogId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Product Backlog")))
                .andExpect(jsonPath("$.priority", is("LOW")))
                .andExpect(jsonPath("$.status", is("DONE")));

        verify(productBacklogService, times(1))
                .updateProductBacklog(eq(backlogId), any(ProductBacklogDTO.class));
    }

    @Test
    void updateProductBacklog_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        long backlogId = 999L;
        when(productBacklogService.updateProductBacklog(eq(backlogId), any(ProductBacklogDTO.class)))
                .thenReturn(null);

        String jsonRequest = """
                {
                    "name": "Updated Backlog",
                    "description": "Description",
                    "priority": "HIGH",
                    "status": "TODO"
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/backlogs/{id}", backlogId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());

        verify(productBacklogService, times(1))
                .updateProductBacklog(eq(backlogId), any(ProductBacklogDTO.class));
    }

    @Test
    void deleteProductBacklog_ShouldReturnNoContent() throws Exception {
        // Given
        long backlogId = 1L;
        when(productBacklogService.deleteProductBacklogById(backlogId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/backlogs/{id}", backlogId))
                .andExpect(status().isNoContent());

        verify(productBacklogService, times(1)).deleteProductBacklogById(backlogId);
    }

    @Test
    void deleteProductBacklog_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        long backlogId = 999L;
        when(productBacklogService.deleteProductBacklogById(backlogId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/backlogs/{id}", backlogId))
                .andExpect(status().isNotFound());

        verify(productBacklogService, times(1)).deleteProductBacklogById(backlogId);
    }

    @Test
    void createProductBacklog_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        when(productBacklogService.createProductBacklog(any(ProductBacklogDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid backlog data"));

        String jsonRequest = """
                {
                    "name": "",
                    "description": "Description",
                    "priority": "HIGH",
                    "status": "TODO"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/backlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(productBacklogService, times(1))
                .createProductBacklog(any(ProductBacklogDTO.class));
    }
}