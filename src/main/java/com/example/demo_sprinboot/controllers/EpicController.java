package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.DTO.EpicDTO;
import com.example.demo_sprinboot.entities.Epic;
import com.example.demo_sprinboot.services.EpicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/epics")
public class EpicController {

    private final EpicService epicService;

    @Autowired
    public EpicController(EpicService epicService) {
        this.epicService = epicService;
    }

    // Endpoint pour récupérer tous les epics
    @GetMapping
    public ResponseEntity<List<EpicDTO>> getAllEpics() {
        List<EpicDTO> epics = epicService.getAllEpics();
        return ResponseEntity.ok(epics);
    }

    // Endpoint pour récupérer un epic par son ID
    @GetMapping("/{id}")
    public ResponseEntity<EpicDTO> getEpicById(@PathVariable long id) {
        EpicDTO epic = epicService.getEpicById(id);
        if (epic != null) {
            return ResponseEntity.ok(epic);
        } else {
            return ResponseEntity.notFound().build();  // 404 si l'epic n'existe pas
        }
    }

    // Endpoint pour créer un nouveau epic
    @PostMapping
    public ResponseEntity<EpicDTO> createEpic(@RequestBody EpicDTO epicDTO) {
        EpicDTO createdEpic = epicService.createEpic(epicDTO);
        return ResponseEntity.status(201).body(createdEpic);  // 201 pour une création réussie
    }

    // Endpoint pour mettre à jour un epic existant
    @PutMapping("/{id}")
    public ResponseEntity<EpicDTO> updateEpic(@PathVariable long id, @RequestBody EpicDTO epicDTO) {
        EpicDTO updatedEpic = epicService.updateEpic(id, epicDTO);
        if (updatedEpic != null) {
            return ResponseEntity.ok(updatedEpic);
        } else {
            return ResponseEntity.notFound().build();  // 404 si l'epic n'existe pas
        }
    }

    // Endpoint pour supprimer un epic
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpic(@PathVariable long id) {
        boolean isDeleted = epicService.deleteEpic(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();  // 204 si la suppression est réussie
        } else {
            return ResponseEntity.notFound().build();  // 404 si l'epic n'existe pas
        }
    }
}
