package com.example.demo_sprinboot.productBacklog;

import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.repository.ProductBacklogRepository;
import com.example.demo_sprinboot.services.ProductBacklogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private ProductBacklogService productBacklogService;

    @Test
    void getAllProductBacklogs() {
        ProductBacklog productBacklog1 = new ProductBacklog("Backlog1");
        ProductBacklog productBacklog2 = new ProductBacklog("Backlog2");

        List<ProductBacklog> productBacklogs = Arrays.asList(productBacklog1, productBacklog2);

        when(productBacklogRepository.findAll()).thenReturn(productBacklogs);
        List<ProductBacklog> result = productBacklogService.getAllProductBacklogs();
        assertNotNull(result);
        assertEquals(productBacklogs, result);
        assertEquals(2, result.size());
        verify(productBacklogRepository, times(1)).findAll();

    }

    @Test
    void getProductBacklogById() {
        ProductBacklog productBacklog1 = new ProductBacklog("Backlog1");
        productBacklog1.setId(1L);
        when(productBacklogRepository.getReferenceById(1L)).thenReturn(productBacklog1);
        ProductBacklog result = productBacklogService.getProductBacklogById(1L);

        assertNotNull(result);
        assertEquals(productBacklog1, result);
        assertEquals(1, result.getId());
        verify(productBacklogRepository, times(1)).getReferenceById(1L);
    }

    @Test
    void createProductBacklog() {
        ProductBacklog productBacklog1 = new ProductBacklog("Backlog1");

        when(productBacklogRepository.save(productBacklog1)).thenReturn(productBacklog1);
        ProductBacklog result = productBacklogService.createProductBacklog(productBacklog1);
        assertNotNull(result);
        assertEquals(productBacklog1, result);
        verify(productBacklogRepository, times(1)).save(productBacklog1);
    }

    @Test
    void updateProductBacklog() {

        ProductBacklog productBacklog1 = new ProductBacklog("Backlog1");
        productBacklog1.setId(1L);
        ProductBacklog productBacklog2 = new ProductBacklog("Backlog2");
        productBacklog2.setId(1L);

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog1));
        when(productBacklogRepository.save(productBacklog1)).thenReturn(productBacklog2);
        ProductBacklog result = productBacklogService.updateProductBacklog(1L, productBacklog2);


        assertNotNull(result);
        assertEquals("Backlog2", result.getName());
        assertEquals(1L, result.getId());


        verify(productBacklogRepository, times(1)).findById(1L);
        verify(productBacklogRepository, times(1)).save(productBacklog1);

    }

    @Test
    void deleteProductBacklogById() {
        ProductBacklog productBacklog1 = new ProductBacklog("Backlog1");
        productBacklog1.setId(1L);
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog1));
        doNothing().when(productBacklogRepository).deleteById(1L);
        productBacklogService.deleteProductBacklogById(1L);
        verify(productBacklogRepository, times(1)).findById(1L);
        verify(productBacklogRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProductBacklogById_NotFound() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            productBacklogService.deleteProductBacklogById(1L);
        });
        verify(productBacklogRepository, times(1)).findById(1L);
        verify(productBacklogRepository, never()).deleteById(any());
    }
}

