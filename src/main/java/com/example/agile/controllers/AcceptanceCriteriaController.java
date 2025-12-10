package com.example.agile.controllers;

import com.example.agile.dto.AcceptanceCriteriaDTO;
import com.example.agile.entities.AcceptanceCriteria;
import com.example.agile.services.AcceptanceCriteriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/acceptance-criteria")
public class AcceptanceCriteriaController {

    private final AcceptanceCriteriaService acceptanceCriteriaService;

    public AcceptanceCriteriaController(AcceptanceCriteriaService acceptanceCriteriaService) {
        this.acceptanceCriteriaService = acceptanceCriteriaService;
    }

    @GetMapping
    public ResponseEntity<List<AcceptanceCriteriaDTO>> getAllAcceptanceCriteria() {
        List<AcceptanceCriteriaDTO> criteriaDTOList = acceptanceCriteriaService.getAllAcceptanceCriterias();
        return ResponseEntity.ok(criteriaDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcceptanceCriteriaDTO> getAcceptanceCriteriaById(@PathVariable Long id) {
        Optional<AcceptanceCriteriaDTO> criteriaDTO = acceptanceCriteriaService.getAcceptanceCriteriaById(id);
        return criteriaDTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AcceptanceCriteriaDTO> createAcceptanceCriteria(@RequestBody AcceptanceCriteria acceptanceCriteria) {
        AcceptanceCriteriaDTO createdCriteria = acceptanceCriteriaService.createAcceptanceCriteria(acceptanceCriteria);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCriteria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcceptanceCriteriaDTO> updateAcceptanceCriteria(@PathVariable Long id,
                                                                          @RequestBody AcceptanceCriteria acceptanceCriteria) {
        AcceptanceCriteriaDTO updatedCriteria = acceptanceCriteriaService.updateAcceptanceCriteria(id, acceptanceCriteria);
        if (updatedCriteria != null) {
            return ResponseEntity.ok(updatedCriteria);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcceptanceCriteria(@PathVariable Long id) {
        if (acceptanceCriteriaService.deleteAcceptanceCriteria(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}