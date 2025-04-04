package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.TaskDTO;
import com.example.demo_sprinboot.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "userStory.id", target = "userStoryId")
    TaskDTO toDto(Task task);

    @Mapping(source = "userStoryId", target = "userStory.id")
    Task toEntity(TaskDTO taskDTO);
    List<TaskDTO> toDto(List<Task> tasks);
}
