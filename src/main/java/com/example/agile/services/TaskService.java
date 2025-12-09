package com.example.agile.services;

import com.example.agile.DTO.TaskDTO;
import java.util.List;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO);

    TaskDTO updateTask(Long id, TaskDTO taskDTO);

    void deleteTask(Long id);

    TaskDTO getTaskById(Long id);

    List<TaskDTO> getAllTasks();
}
