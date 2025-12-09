package com.example.agile.services;

import com.example.agile.DTO.EpicDTO;
import java.util.List;

public interface EpicService {
    EpicDTO createEpic(EpicDTO epicDTO);
    EpicDTO updateEpic(Long id, EpicDTO epicDTO);
    boolean deleteEpic(Long id);
    EpicDTO getEpicById(Long id);
    List<EpicDTO> getAllEpics();
}