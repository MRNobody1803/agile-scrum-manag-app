package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.SprintDTO;
import com.example.demo_sprinboot.entities.Sprint;
import com.example.demo_sprinboot.entities.Status;
import com.example.demo_sprinboot.mappers.SprintMapper;
import com.example.demo_sprinboot.repository.SprintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SprintServiceTest {

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private SprintMapper sprintMapper;

    @InjectMocks
    private SprintServiceImpl sprintService;

    private Sprint sprint;
    private SprintDTO sprintDTO;

    @BeforeEach
    void setUp() {
        sprint = new Sprint();
        sprint.setId(1L);
        sprint.setName("Sprint 1");
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusDays(14));
        sprint.setStatus(Status.IN_PROGRESS);

        sprintDTO = new SprintDTO();
        sprintDTO.setId(1L);
        sprintDTO.setName("Sprint 1");
        sprintDTO.setStartDate(LocalDate.now());
        sprintDTO.setEndDate(LocalDate.now().plusDays(14));
        sprintDTO.setStatus(Status.IN_PROGRESS);
    }

    @Test
    void testGetAllSprints() {
        when(sprintRepository.findAll()).thenReturn(Arrays.asList(sprint));
        when(sprintMapper.toDto(sprint)).thenReturn(sprintDTO);

        List<SprintDTO> result = sprintService.getAllSprints();

        assertEquals(1, result.size());
        assertEquals("Sprint 1", result.get(0).getName());
        verify(sprintRepository, times(1)).findAll();
    }

    @Test
    void testGetSprintById() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(sprintMapper.toDto(sprint)).thenReturn(sprintDTO);

        Optional<SprintDTO> result = sprintService.getSprintById(1L);

        assertTrue(result.isPresent());
        assertEquals("Sprint 1", result.get().getName());
        verify(sprintRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateSprint() {
        when(sprintMapper.toEntity(sprintDTO)).thenReturn(sprint);
        when(sprintRepository.save(sprint)).thenReturn(sprint);
        when(sprintMapper.toDto(sprint)).thenReturn(sprintDTO);

        SprintDTO result = sprintService.createSprint(sprintDTO);

        assertNotNull(result);
        assertEquals("Sprint 1", result.getName());
        verify(sprintRepository, times(1)).save(sprint);
    }

    @Test
    void testUpdateSprint() {
        SprintDTO updatedDTO = new SprintDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Sprint");
        updatedDTO.setStartDate(LocalDate.now());
        updatedDTO.setEndDate(LocalDate.now().plusDays(14));
        updatedDTO.setStatus(Status.IN_PROGRESS);

        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);
        when(sprintMapper.toDto(any(Sprint.class))).thenReturn(updatedDTO);

        SprintDTO result = sprintService.updateSprint(1L, updatedDTO);

        assertEquals("Updated Sprint", result.getName());
        assertEquals(Status.IN_PROGRESS, result.getStatus());
        verify(sprintRepository, times(1)).findById(1L);
        verify(sprintRepository, times(1)).save(any(Sprint.class));
    }

    @Test
    void testDeleteSprint() {
        when(sprintRepository.existsById(1L)).thenReturn(true);
        doNothing().when(sprintRepository).deleteById(1L);

        sprintService.deleteSprint(1L);

        verify(sprintRepository, times(1)).deleteById(1L);
    }

    @Test
    void testStartSprint() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        sprintService.startSprint(1L);

        assertEquals(Status.IN_PROGRESS, sprint.getStatus());
        verify(sprintRepository, times(1)).findById(1L);
        verify(sprintRepository, times(1)).save(sprint);
    }

    @Test
    void testCompleteSprint() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        sprintService.completeSprint(1L);

        assertEquals(Status.DONE, sprint.getStatus());
        verify(sprintRepository, times(1)).findById(1L);
        verify(sprintRepository, times(1)).save(sprint);
    }
}
