//package com.example.demo_sprinboot.controllers;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//import com.example.demo_sprinboot.services.TaskService;
//import com.example.demo_sprinboot.services.TaskServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//
//import java.util.Collections;
//
//@WebMvcTest(TaskController.class)
//public class TaskControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private TaskServiceImpl taskService; // Mock de TaskService
//
//    @InjectMocks
//    private TaskController taskController; // Injection manuelle du contrôleur avec le service mocké
//
//    @BeforeEach
//    void setUp() {
//        // Configurer MockMvc avec le contrôleur
//        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
//    }
//
//    @Test
//    public void testGetAllTasks() throws Exception {
//        // Simuler un retour de service
//        Mockito.when(taskService.getAllTasks()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/tasks"))
//                .andExpect(status().isOk()) // Vérifier le code HTTP
//                .andExpect(content().contentType(APPLICATION_JSON)); // Vérifier le type de contenu
//    }
//}
