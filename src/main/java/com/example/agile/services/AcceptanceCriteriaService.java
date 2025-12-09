package com.example.agile.services;

import com.example.agile.DTO.AcceptanceCriteriaDTO;
import com.example.agile.entities.AcceptanceCriteria;

import java.util.List;
import java.util.Optional;

public interface AcceptanceCriteriaService {

    Optional<AcceptanceCriteriaDTO> getAcceptanceCriteriaById(Long id);

    AcceptanceCriteriaDTO createAcceptanceCriteria(AcceptanceCriteria acceptanceCriteria);

    AcceptanceCriteriaDTO updateAcceptanceCriteria(Long id, AcceptanceCriteria acceptanceCriteria);

    boolean deleteAcceptanceCriteria(Long id);

    List<AcceptanceCriteriaDTO> getAllAcceptanceCriterias();
}
