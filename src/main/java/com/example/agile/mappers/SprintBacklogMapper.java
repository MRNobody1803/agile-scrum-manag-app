package com.example.agile.mappers;

import com.example.agile.dto.SprintBacklogDTO;
import com.example.agile.entities.SprintBacklog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SprintBacklogMapper {
    SprintBacklogDTO toDto(SprintBacklog sprintBacklog);
    SprintBacklog toEntity(SprintBacklogDTO sprintBacklogDTO);
}
