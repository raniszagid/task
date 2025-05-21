package com.example.task_app.service;

import com.example.task_app.kafka.UpdateLogProducer;
import com.example.task_app.mapper.TaskMapper;
import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.model.enums.TaskStatus;
import com.example.task_app.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SpringTaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskRepository taskRepository;

    @MockitoBean
    private UpdateLogProducer updateLogProducer;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void findById() {
        Task task = Task.builder().title("title").status(TaskStatus.NEW).build();
        //when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        taskRepository.save(task);
        TaskService taskService = new TaskService(taskRepository, taskMapper, updateLogProducer);
        TaskDto taskDto = taskService.findById(1L);

        assertNotNull(taskDto);
        assertEquals("title", taskDto.getTitle());
        assertEquals("NEW", taskDto.getStatus());
    }
}
