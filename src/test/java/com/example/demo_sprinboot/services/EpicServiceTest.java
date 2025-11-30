package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.EpicDTO;
import com.example.demo_sprinboot.entities.Epic;
import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.exceptions.ResourceNotFoundException;
import com.example.demo_sprinboot.mappers.EpicMapper;
import com.example.demo_sprinboot.repository.EpicRepository;
import com.example.demo_sprinboot.repository.ProductBacklogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EpicServiceTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private EpicMapper epicMapper;

    @InjectMocks
    private EpicServiceImpl epicService;

    private Epic epic;
    private EpicDTO epicDTO;
    private ProductBacklog productBacklog;

    @BeforeEach
    void setUp() {
        productBacklog = new ProductBacklog();
        productBacklog.setId(1L);

        epic = new Epic();
        epic.setId(1L);
        epic.setName("Epic Test");
        epic.setProductBacklog(productBacklog);

        epicDTO = new EpicDTO();
        epicDTO.setId(1L);
        epicDTO.setName("Epic Test");
        epicDTO.setProductBacklogId(1L);
    }

    @Test
    void testCreateEpic() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(epicMapper.toEntity(any(EpicDTO.class))).thenReturn(epic);
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);
        when(epicMapper.toDTO(any(Epic.class))).thenReturn(epicDTO);

        EpicDTO result = epicService.createEpic(epicDTO);

        assertNotNull(result);
        assertEquals("Epic Test", result.getName());
        verify(productBacklogRepository, times(1)).findById(1L);
        verify(epicRepository, times(1)).save(any(Epic.class));
    }

    @Test
    void testCreateEpic_ProductBacklogNotFound() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            epicService.createEpic(epicDTO);
        });

        assertEquals("ProductBacklog not found", exception.getMessage());
    }

    @Test
    void testUpdateEpic() {
        // Arrange: Mock pour l'épic existant
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

        // Ajout de cette ligne pour éviter l'erreur "ProductBacklog not found"
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));

        when(epicRepository.save(any(Epic.class))).thenReturn(epic);
        when(epicMapper.toDTO(any(Epic.class))).thenReturn(epicDTO);

        // Act
        EpicDTO result = epicService.updateEpic(1L, epicDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Epic Test", result.getName());
        verify(epicRepository, times(1)).save(any(Epic.class));
    }


    @Test
    void testUpdateEpic_NotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            epicService.updateEpic(1L, epicDTO);
        });

        assertEquals("Epic not found", exception.getMessage());
    }

    @Test
    void testDeleteEpic() {
        // Arrange: Création d'un mock de Epic
        Epic epicMock = mock(Epic.class);

        // Simulation : Lorsque findById est appelé, il retourne epicMock
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epicMock));

        // Simulation : epicMock.getUserStories() retourne une liste vide (pas de User Stories)
        when(epicMock.getUserStories()).thenReturn(List.of());

        // Act: Suppression de l'Epic
        boolean result = epicService.deleteEpic(1L);

        // Assert: Vérifier que l'Epic a bien été supprimé
        assertFalse(result);
        verify(epicRepository, times(1)).delete(any(Epic.class));
    }


    @Test
    void testDeleteEpic_WithUserStories() {
        Epic epicMock = mock(Epic.class);

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epicMock));

        when(epicMock.getUserStories()).thenReturn(List.of(mock(com.example.demo_sprinboot.entities.UserStory.class)));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            epicService.deleteEpic(1L);
        });

        assertEquals("Cannot delete Epic with existing User Stories", exception.getMessage());
    }

    @Test
    void testGetEpicById() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicMapper.toDTO(any(Epic.class))).thenReturn(epicDTO);

        EpicDTO result = epicService.getEpicById(1L);

        assertNotNull(result);
        assertEquals("Epic Test", result.getName());
    }

    @Test
    void testGetAllEpics() {
        when(epicRepository.findAll()).thenReturn(Arrays.asList(epic));
        when(epicMapper.toDTO(any(Epic.class))).thenReturn(epicDTO);

        List<EpicDTO> result = epicService.getAllEpics();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
