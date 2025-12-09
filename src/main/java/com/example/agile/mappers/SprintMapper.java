package com.example.agile.mappers;

import com.example.agile.DTO.SprintDTO;
import com.example.agile.entities.Sprint;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SprintMapper {
    SprintDTO toDto(Sprint sprint);
    Sprint toEntity(SprintDTO sprintDTO);
}
