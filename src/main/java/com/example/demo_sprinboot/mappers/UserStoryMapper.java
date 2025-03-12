package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.UserStoryDTO;
import com.example.demo_sprinboot.entities.UserStory;
import org.mapstruct.Mapper;

@Mapper
public interface UserStoryMapper {
    UserStoryDTO toDto(UserStory userStory);
    UserStory toEntity(UserStoryDTO userStoryDTO);
}
