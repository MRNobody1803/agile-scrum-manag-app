package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.TaskDTO;
import com.example.demo_sprinboot.entities.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapper {
    TaskDTO toDto(Task task);
    Task toEntity(TaskDTO taskDTO);
}
