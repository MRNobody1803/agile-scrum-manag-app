package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.SprintBacklogDTO;
import com.example.demo_sprinboot.entities.SprintBacklog;
import com.example.demo_sprinboot.mappers.SprintBacklogMapper;
import com.example.demo_sprinboot.repository.SprintBacklogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SprintBacklogServiceTest {

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private SprintBacklogMapper sprintBacklogMapper;

    @InjectMocks
    private SprintBacklogServiceImpl sprintBacklogService;

    private SprintBacklog sprintBacklog;
    private SprintBacklogDTO sprintBacklogDTO;

    @BeforeEach
    void setUp() {
        sprintBacklog = new SprintBacklog();
        sprintBacklog.setId(1L);
        sprintBacklog.setName("Sprint 1 Backlog");

        sprintBacklogDTO = new SprintBacklogDTO();
        sprintBacklogDTO.setId(1L);
        sprintBacklogDTO.setName("Sprint 1 Backlog");
    }

    @Test
    void testGetAllSprintBacklogs() {
        when(sprintBacklogRepository.findAll()).thenReturn(Arrays.asList(sprintBacklog));
        when(sprintBacklogMapper.toDto(sprintBacklog)).thenReturn(sprintBacklogDTO);

        List<SprintBacklogDTO> result = sprintBacklogService.getAllSprintBacklogs();

        assertEquals(1, result.size());
        assertEquals("Sprint 1 Backlog", result.get(0).getName());
    }

    @Test
    void testGetSprintBacklogById() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(sprintBacklogMapper.toDto(sprintBacklog)).thenReturn(sprintBacklogDTO);

        Optional<SprintBacklogDTO> result = sprintBacklogService.getSprintBacklogById(1L);

        assertTrue(result.isPresent());
        assertEquals("Sprint 1 Backlog", result.get().getName());
    }

    @Test
    void testCreateSprintBacklog() {
        when(sprintBacklogMapper.toEntity(sprintBacklogDTO)).thenReturn(sprintBacklog);
        when(sprintBacklogRepository.save(sprintBacklog)).thenReturn(sprintBacklog);
        when(sprintBacklogMapper.toDto(sprintBacklog)).thenReturn(sprintBacklogDTO);

        SprintBacklogDTO result = sprintBacklogService.createSprintBacklog(sprintBacklogDTO);

        assertNotNull(result);
        assertEquals("Sprint 1 Backlog", result.getName());
    }

    @Test
    void testUpdateSprintBacklog() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(sprintBacklogMapper.toEntity(sprintBacklogDTO)).thenReturn(sprintBacklog); // 👈 ligne ajoutée
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprintBacklog);
        when(sprintBacklogMapper.toDto(any(SprintBacklog.class))).thenReturn(sprintBacklogDTO);

        SprintBacklogDTO result = sprintBacklogService.updateSprintBacklog(1L, sprintBacklogDTO);

        assertNotNull(result);
        assertEquals("Sprint 1 Backlog", result.getName());
    }



    @Test
    void testDeleteSprintBacklog() {
        when(sprintBacklogRepository.existsById(1L)).thenReturn(true);
        doNothing().when(sprintBacklogRepository).deleteById(1L);

        assertDoesNotThrow(() -> sprintBacklogService.deleteSprintBacklog(1L));
    }
}
