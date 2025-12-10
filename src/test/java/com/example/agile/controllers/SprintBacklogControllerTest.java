package com.example.agile.controllers;

import com.example.agile.dto.SprintBacklogDTO;
import com.example.agile.exceptions.GlobalExceptionHandler;
import com.example.agile.services.SprintBacklogService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SprintBacklogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SprintBacklogService sprintBacklogService;

    @InjectMocks
    private SprintBacklogController sprintBacklogController;

    private ObjectMapper objectMapper;

    private SprintBacklogDTO sprintBacklogDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sprintBacklogController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        sprintBacklogDTO = new SprintBacklogDTO(
                1L,
                "Sprint Backlog 1",
                Arrays.asList(1L, 2L, 3L),
                10L
        );
    }

    @Test
    void getAllSprintBacklogs_ShouldReturnList() throws Exception {
        SprintBacklogDTO dto2 = new SprintBacklogDTO(
                2L,
                "Sprint Backlog 2",
                Arrays.asList(4L, 5L),
                20L
        );

        List<SprintBacklogDTO> list = Arrays.asList(sprintBacklogDTO, dto2);

        when(sprintBacklogService.getAllSprintBacklogs()).thenReturn(list);

        mockMvc.perform(get("/api/sprint-backlogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Sprint Backlog 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Sprint Backlog 2")));

        verify(sprintBacklogService, times(1)).getAllSprintBacklogs();
    }

    @Test
    void getSprintBacklogById_ShouldReturnBacklog() throws Exception {
        long id = 1L;

        when(sprintBacklogService.getSprintBacklogById(id))
                .thenReturn(Optional.of(sprintBacklogDTO));

        mockMvc.perform(get("/api/sprint-backlogs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Sprint Backlog 1")));

        verify(sprintBacklogService, times(1)).getSprintBacklogById(id);
    }

    @Test
    void getSprintBacklogById_WhenNotFound_ShouldReturnOkWithEmpty() throws Exception {
        long id = 999L;

        when(sprintBacklogService.getSprintBacklogById(id))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sprint-backlogs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(sprintBacklogService, times(1)).getSprintBacklogById(id);
    }

    @Test
    void createSprintBacklog_ShouldReturnCreated() throws Exception {
        when(sprintBacklogService.createSprintBacklog(any(SprintBacklogDTO.class)))
                .thenReturn(sprintBacklogDTO);

        String jsonRequest = """
            {
                "name": "Sprint Backlog 1",
                "userStoryIds": [1, 2, 3],
                "sprintId": 10
            }
            """;

        mockMvc.perform(post("/api/sprint-backlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Sprint Backlog 1")));

        verify(sprintBacklogService, times(1))
                .createSprintBacklog(any(SprintBacklogDTO.class));
    }

    @Test
    void updateSprintBacklog_ShouldReturnUpdated() throws Exception {
        long id = 1L;

        SprintBacklogDTO updated = new SprintBacklogDTO(
                id, "Updated Sprint", List.of(9L, 8L), 99L
        );

        when(sprintBacklogService.updateSprintBacklog(eq(id), any(SprintBacklogDTO.class)))
                .thenReturn(updated);

        String body = """
            {
              "name": "Updated Sprint",
              "userStoryIds": [9, 8],
              "sprintId": 99
            }
            """;

        mockMvc.perform(put("/api/sprint-backlogs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Sprint")))
                .andExpect(jsonPath("$.sprintId", is(99)));

        verify(sprintBacklogService, times(1))
                .updateSprintBacklog(eq(id), any(SprintBacklogDTO.class));
    }

    @Test
    void deleteSprintBacklog_ShouldReturnNoContent() throws Exception {
        long id = 1L;

        doNothing().when(sprintBacklogService).deleteSprintBacklog(id);

        mockMvc.perform(delete("/api/sprint-backlogs/{id}", id))
                .andExpect(status().isNoContent());

        verify(sprintBacklogService, times(1)).deleteSprintBacklog(id);
    }

    @Test
    void updateSprintBacklogName_ShouldReturnOk() throws Exception {
        long id = 1L;
        String newName = "New Name";

        doNothing().when(sprintBacklogService).updateSprintBacklogName(id, newName);

        mockMvc.perform(put("/api/sprint-backlogs/{id}/name", id)
                        .param("newName", newName))
                .andExpect(status().isOk());

        verify(sprintBacklogService, times(1))
                .updateSprintBacklogName(id, newName);
    }

    @Test
    void searchSprintBacklogs_ShouldReturnList() throws Exception {
        String keyword = "Sprint";

        when(sprintBacklogService.searchSprintBacklogByName(keyword))
                .thenReturn(List.of(sprintBacklogDTO));

        mockMvc.perform(get("/api/sprint-backlogs/search")
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Sprint Backlog 1")));

        verify(sprintBacklogService, times(1))
                .searchSprintBacklogByName(keyword);
    }
}
