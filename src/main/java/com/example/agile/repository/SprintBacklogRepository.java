package com.example.agile.repository;

import com.example.agile.entities.SprintBacklog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long> {
    List<SprintBacklog> findBySprintId(Long sprintId);
    @Modifying
    @Transactional
    @Query("UPDATE SprintBacklog sb SET sb.name = :newName WHERE sb.id = :id")
    void updateSprintBacklogName(@Param("id") Long id, @Param("newName") String newName);

    // Trouver les SprintBacklogs contenant un certain mot-clé dans leur nom
    @Query("SELECT sb FROM SprintBacklog sb WHERE LOWER(sb.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SprintBacklog> searchByNom(@Param("keyword") String keyword);

}
