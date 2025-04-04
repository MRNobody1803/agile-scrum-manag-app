package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.UserStoryDTO;
import com.example.demo_sprinboot.entities.*;
import com.example.demo_sprinboot.exceptions.ResourceNotFoundException;
import com.example.demo_sprinboot.mappers.UserStoryMapper;
import com.example.demo_sprinboot.repository.EpicRepository;
import com.example.demo_sprinboot.repository.SprintBacklogRepository;
import com.example.demo_sprinboot.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private UserStoryServiceImpl userStoryService;

    private UserStory userStory;
    private UserStoryDTO userStoryDTO;
    private Epic epic;
    private SprintBacklog sprintBacklog;

    @BeforeEach
    void setUp() {
        // Création de l'objet UserStory
        userStory = new UserStory();
        userStory.setId(1L);
        userStory.setTitle("Sample User Story");
        userStory.setDescription("This is a test description");

        // DTO associé
        userStoryDTO = new UserStoryDTO();
        userStoryDTO.setId(1L);
        userStoryDTO.setTitle("Sample User Story");
        userStoryDTO.setDescription("This is a test description");

        // Epic et SprintBacklog
        epic = new Epic();
        epic.setId(1L);

        sprintBacklog = new SprintBacklog();
        sprintBacklog.setId(1L);
    }

    @Test
    void testGetAllUserStories() {
        when(userStoryRepository.findAll()).thenReturn(List.of(userStory));
        when(userStoryMapper.toDto(userStory)).thenReturn(userStoryDTO);

        List<UserStoryDTO> result = userStoryService.getAllUserStories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sample User Story", result.get(0).getTitle());
        verify(userStoryRepository, times(1)).findAll();
    }

    @Test
    void testGetUserStoryById() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryMapper.toDto(userStory)).thenReturn(userStoryDTO);

        UserStoryDTO result = userStoryService.getUserStoryById(1L);

        assertNotNull(result);
        assertEquals("Sample User Story", result.getTitle());
        verify(userStoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserStoryById_NotFound() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userStoryService.getUserStoryById(1L);
        });

        assertEquals("User story with id 1 not found", exception.getMessage());
    }

    @Test
    void testCreateUserStory() {
        when(userStoryMapper.toEntity(any(UserStoryDTO.class))).thenReturn(userStory);
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);
        when(userStoryMapper.toDto(any(UserStory.class))).thenReturn(userStoryDTO);

        UserStoryDTO result = userStoryService.createUserStory(userStoryDTO);

        assertNotNull(result);
        assertEquals("Sample User Story", result.getTitle());
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    @Test
    void testUpdateUserStory() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);
        when(userStoryMapper.toDto(any(UserStory.class))).thenReturn(userStoryDTO);

        UserStoryDTO result = userStoryService.updateUserStory(1L, userStoryDTO);

        assertNotNull(result);
        assertEquals("Sample User Story", result.getTitle());
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    @Test
    void testUpdateUserStory_NotFound() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userStoryService.updateUserStory(1L, userStoryDTO);
        });

        assertEquals("User story with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteUserStoryById() {
        when(userStoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userStoryRepository).deleteById(1L);

        assertDoesNotThrow(() -> userStoryService.deleteUserStoryById(1L));

        verify(userStoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserStoryById_NotFound() {
        when(userStoryRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userStoryService.deleteUserStoryById(1L);
        });

        assertEquals("User story with id 1 not found", exception.getMessage());
    }

    @Test
    void testUpdatePriority() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);
        when(userStoryMapper.toDto(any(UserStory.class))).thenReturn(userStoryDTO);

        UserStoryDTO result = userStoryService.updatePriority(1L, "HIGH");

        assertNotNull(result);
        assertEquals("Sample User Story", result.getTitle());
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    @Test
    void testUpdatePriority_InvalidValue() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.updatePriority(1L, "INVALID_PRIORITY");
        });

        assertEquals("Invalid priority value: INVALID_PRIORITY", exception.getMessage());
    }

}
