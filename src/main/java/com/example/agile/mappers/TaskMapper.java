package com.example.agile.mappers;

import com.example.agile.dto.TaskDTO;
import com.example.agile.entities.Task;
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
