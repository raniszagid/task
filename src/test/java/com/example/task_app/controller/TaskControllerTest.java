package com.example.task_app.controller;

import com.example.task_app.model.Task;
import com.example.task_app.model.enums.TaskStatus;
import com.example.task_app.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        saveTestData();
    }

    @Test
    void getCertainTask() throws Exception {
        mvc.perform(get("/tasks/2")).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Task 2"));
        mvc.perform(get("/tasks/8")).andExpect(status().isNotFound());
    }

    @Test
    void getAllTasks() throws Exception {
        mvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void createTask() throws Exception {
        mvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getTaskJson()))
                .andExpect(status().isOk());
    }

    @Test
    void updateTask() throws Exception {
        mvc.perform(put("/tasks/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getTaskJson()))
                .andExpect(status().isOk());
        mvc.perform(put("/tasks/67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getTaskJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask() throws Exception {
        mvc.perform(delete("/tasks/2"))
                .andExpect(status().isOk());
    }

    private void saveTestData() {
        Task task1 = Task.builder().title("Task 1").description("Task1 description").status(TaskStatus.NEW).build();
        Task task2 = Task.builder().title("Task 2").description("Task2 description").status(TaskStatus.PROCESSING).build();
        Task task3 = Task.builder().title("Task 3").description("Task3 description").status(TaskStatus.PROCESSING).build();
        repository.saveAll(List.of(task1, task2, task3));
    }

    private String getTaskJson() {
        return """
        {
            "title": "New Task",
            "description": "Description",
            "status": "NEW"
        }
        """;
    }
}
