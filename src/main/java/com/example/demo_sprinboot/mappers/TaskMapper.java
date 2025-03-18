package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.TaskDTO;
import com.example.demo_sprinboot.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDTO toDto(Task task);
    Task toEntity(TaskDTO taskDTO);
}
