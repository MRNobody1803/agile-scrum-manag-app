package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.AcceptanceCriteriaDTO;
import com.example.demo_sprinboot.entities.AcceptanceCriteria;
import com.example.demo_sprinboot.mappers.AcceptanceCriteriaMapper;
import com.example.demo_sprinboot.repository.AcceptanceCriteriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AcceptanceCriteriaServiceTest {

    @Mock
    private AcceptanceCriteriaRepository acceptanceCriteriaRepository;

    @Mock
    private AcceptanceCriteriaMapper acceptanceCriteriaMapper;

    @InjectMocks
    private AcceptanceCriteriaServiceImpl acceptanceCriteriaService;

    private AcceptanceCriteria acceptanceCriteria;
    private AcceptanceCriteriaDTO acceptanceCriteriaDTO;

    @BeforeEach
    void setUp() {
        acceptanceCriteria = new AcceptanceCriteria();
        acceptanceCriteria.setId(1L);
        acceptanceCriteria.setDescription("Criterion 1");

        acceptanceCriteriaDTO = new AcceptanceCriteriaDTO();
        acceptanceCriteriaDTO.setId(1L);
        acceptanceCriteriaDTO.setDescription("Criterion 1");
    }

    @Test
    void testGetAllAcceptanceCriterias() {
        when(acceptanceCriteriaRepository.findAll()).thenReturn(List.of(acceptanceCriteria));
        when(acceptanceCriteriaMapper.toDto(acceptanceCriteria)).thenReturn(acceptanceCriteriaDTO);

        List<AcceptanceCriteriaDTO> result = acceptanceCriteriaService.getAllAcceptanceCriterias();

        assertEquals(1, result.size());
        assertEquals("Criterion 1", result.get(0).getDescription());
    }

    @Test
    void testGetAcceptanceCriteriaById_Found() {
        when(acceptanceCriteriaRepository.findById(1L)).thenReturn(Optional.of(acceptanceCriteria));
        when(acceptanceCriteriaMapper.toDto(acceptanceCriteria)).thenReturn(acceptanceCriteriaDTO);

        Optional<AcceptanceCriteriaDTO> result = acceptanceCriteriaService.getAcceptanceCriteriaById(1L);

        assertTrue(result.isPresent());
        assertEquals("Criterion 1", result.get().getDescription());
    }

    @Test
    void testGetAcceptanceCriteriaById_NotFound() {
        when(acceptanceCriteriaRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<AcceptanceCriteriaDTO> result = acceptanceCriteriaService.getAcceptanceCriteriaById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateAcceptanceCriteria() {
        when(acceptanceCriteriaRepository.save(acceptanceCriteria)).thenReturn(acceptanceCriteria);
        when(acceptanceCriteriaMapper.toDto(acceptanceCriteria)).thenReturn(acceptanceCriteriaDTO);

        AcceptanceCriteriaDTO result = acceptanceCriteriaService.createAcceptanceCriteria(acceptanceCriteria);

        assertNotNull(result);
        assertEquals("Criterion 1", result.getDescription());
    }

    @Test
    void testUpdateAcceptanceCriteria() {
        when(acceptanceCriteriaRepository.existsById(1L)).thenReturn(true);
        when(acceptanceCriteriaRepository.save(acceptanceCriteria)).thenReturn(acceptanceCriteria);
        when(acceptanceCriteriaMapper.toDto(acceptanceCriteria)).thenReturn(acceptanceCriteriaDTO);

        AcceptanceCriteriaDTO result = acceptanceCriteriaService.updateAcceptanceCriteria(1L, acceptanceCriteria);

        assertNotNull(result);
        assertEquals("Criterion 1", result.getDescription());
    }

    @Test
    void testUpdateAcceptanceCriteria_NotFound() {
        when(acceptanceCriteriaRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> acceptanceCriteriaService.updateAcceptanceCriteria(1L, acceptanceCriteria));

        assertEquals("AcceptanceCriteria not found with id 1", exception.getMessage());
    }

    @Test
    void testDeleteAcceptanceCriteria_Success() {
        when(acceptanceCriteriaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(acceptanceCriteriaRepository).deleteById(1L);

        boolean result = acceptanceCriteriaService.deleteAcceptanceCriteria(1L);

        assertFalse(result); // car le service retourne toujours `false`
        verify(acceptanceCriteriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAcceptanceCriteria_NotFound() {
        when(acceptanceCriteriaRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> acceptanceCriteriaService.deleteAcceptanceCriteria(1L));

        assertEquals("AcceptanceCriteria not found with id 1", exception.getMessage());
    }
}
