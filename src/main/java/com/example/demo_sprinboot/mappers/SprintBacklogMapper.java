package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.SprintBacklogDTO;
import com.example.demo_sprinboot.entities.SprintBacklog;
import org.mapstruct.Mapper;

@Mapper
public interface SprintBacklogMapper {
    SprintBacklogDTO toDto(SprintBacklog sprintBacklog);
    SprintBacklog toEntity(SprintBacklogDTO sprintBacklogDTO);
}
