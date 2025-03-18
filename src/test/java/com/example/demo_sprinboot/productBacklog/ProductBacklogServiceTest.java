package com.example.demo_sprinboot.productBacklog;

import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.repository.ProductBacklogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // Charge uniquement la couche JPA avec H2
class ProductBacklogRepositoryTest {

    @Autowired
    private ProductBacklogRepository productBacklogRepository;

    @BeforeEach
    void setUp() {
        productBacklogRepository.deleteAll(); // Nettoyer la BD avant chaque test
    }

    @Test
    void testSaveAndFindProductBacklog() {
        // 🔹 Créer et sauvegarder un backlog
        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setName("Backlog Test");
        productBacklogRepository.save(productBacklog);

        // 🔹 Récupérer les backlogs
        List<ProductBacklog> backlogs = productBacklogRepository.findAll();

        // ✅ Vérification
        assertEquals(1, backlogs.size());
        assertEquals("Backlog Test", backlogs.get(0).getName());
    }

    @Test
    void testDeleteProductBacklog() {
        // 🔹 Ajouter un backlog
        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setName("Backlog to Delete");
        productBacklog = productBacklogRepository.save(productBacklog);

        // 🔹 Supprimer le backlog
        productBacklogRepository.deleteById(productBacklog.getId());

        // ✅ Vérification
        assertFalse(productBacklogRepository.findById(productBacklog.getId()).isPresent());
    }
}
