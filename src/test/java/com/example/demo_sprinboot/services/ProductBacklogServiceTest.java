package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.ProductBacklogDTO;
import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.mappers.ProductBacklogMapper;
import com.example.demo_sprinboot.repository.ProductBacklogRepository;
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
class ProductBacklogServiceTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private ProductBacklogServiceImpl productBacklogService;

    private ProductBacklog productBacklog;
    private ProductBacklogDTO productBacklogDTO;

    @BeforeEach
    void setUp() {
        productBacklog = new ProductBacklog();
        productBacklog.setId(1L);
        productBacklog.setName("Feature X");

        productBacklogDTO = new ProductBacklogDTO();
        productBacklogDTO.setId(1L);
        productBacklogDTO.setName("Feature X");
    }

    @Test
    void testGetAllProductBacklogs() {
        when(productBacklogRepository.findAll()).thenReturn(Arrays.asList(productBacklog));

        List<ProductBacklogDTO> result = productBacklogService.getAllProductBacklogs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Feature X", result.get(0).getName());

        verify(productBacklogRepository, times(1)).findAll();
    }

    @Test
    void testGetProductBacklogById() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));

        ProductBacklogDTO result = productBacklogService.getProductBacklogById(1L);

        assertNotNull(result);
        assertEquals("Feature X", result.getName());

        verify(productBacklogRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProductBacklog() {
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(productBacklog);

        ProductBacklogDTO result = productBacklogService.createProductBacklog(productBacklogDTO);

        assertNotNull(result);
        assertEquals("Feature X", result.getName());

        verify(productBacklogRepository, times(1)).save(any(ProductBacklog.class));
    }

    @Test
    void testUpdateProductBacklog() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(productBacklog);

        ProductBacklogDTO updatedDTO = new ProductBacklogDTO();
        updatedDTO.setName("Updated Feature X");

        ProductBacklogDTO result = productBacklogService.updateProductBacklog(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Feature X", result.getName());

        verify(productBacklogRepository, times(1)).findById(1L);
        verify(productBacklogRepository, times(1)).save(any(ProductBacklog.class));
    }

    @Test
    void testDeleteProductBacklogById() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        doNothing().when(productBacklogRepository).deleteById(1L);

        boolean result = productBacklogService.deleteProductBacklogById(1L);

        assertTrue(result);
        verify(productBacklogRepository, times(1)).deleteById(1L);
    }
}
