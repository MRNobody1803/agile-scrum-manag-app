package com.example.demo_sprinboot.DTO;

import com.example.demo_sprinboot.DTO.UserStoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EpicDTO {
    private Long id;
    private String name;
    private String description;
    private Long productBacklogId;
    private List<UserStoryDTO> userStoryList;
}
