//package com.example.demo_sprinboot.services;
//
//import com.example.demo_sprinboot.DTO.TaskDTO;
//import com.example.demo_sprinboot.entities.Status;
//import com.example.demo_sprinboot.entities.Task;
//import com.example.demo_sprinboot.mappers.TaskMapper;
//import com.example.demo_sprinboot.repository.TaskRepository;
//import com.example.demo_sprinboot.repository.UserStoryRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@ExtendWith(MockitoExtension.class)
//public class TaskServiceTest {
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private TaskMapper taskMapper;
//
//    @Mock
//    private UserStoryRepository userStoryRepository;
//
//    @InjectMocks
//    private TaskServiceImpl taskService;
//
//    @Test
//    public void testCreateTask() {
//        // Créer un TaskDTO (comme attendu par createTask)
//        TaskDTO taskDTO = new TaskDTO();
//        taskDTO.setName("Test Task");
//        taskDTO.setDescription("Description");
//
//        // Convertir TaskDTO en Task via TaskMapper
//        Task task = new Task(1L, "Test Task", "Description", Status.TODO, null);
//
//        // Simuler le comportement du mapper et du repository
//        Mockito.when(taskMapper.toEntity(Mockito.any(TaskDTO.class))).thenReturn(task);
//        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
//        Mockito.when(userStoryRepository.findById(Mockito.any())).thenReturn(Optional.empty());
//        // Appeler la méthode avec un TaskDTO
//        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);
//
//        // Vérifier le résultat
//        assertNotNull(createdTaskDTO);
//        assertEquals("Test Task", createdTaskDTO.getName());
//    }
//}
//
//
