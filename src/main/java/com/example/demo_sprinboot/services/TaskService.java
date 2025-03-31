package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.TaskDTO;
import java.util.List;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO);

    TaskDTO updateTask(Long id, TaskDTO taskDTO);

    void deleteTask(Long id);

    TaskDTO getTaskById(Long id);

    List<TaskDTO> getAllTasks();
}
