package com.example.agile.mappers;

import com.example.agile.dto.AcceptanceCriteriaDTO;
import com.example.agile.entities.AcceptanceCriteria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AcceptanceCriteriaMapper {
    AcceptanceCriteriaDTO toDto(AcceptanceCriteria acceptanceCriteria);
    AcceptanceCriteria toEntity(AcceptanceCriteriaDTO acceptanceCriteriaDTO);
}
