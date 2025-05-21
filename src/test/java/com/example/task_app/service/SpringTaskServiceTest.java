package com.example.task_app.service;

import com.example.task_app.kafka.UpdateLogProducer;
import com.example.task_app.mapper.TaskMapper;
import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.model.enums.TaskStatus;
import com.example.task_app.repository.TaskRepository;
import com.example.task_app.validation.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    void saveTask() {
        Task task = Task.builder().title("Task").status(TaskStatus.FINISHED).build();
        taskService.save(task);
        assertEquals("Task", taskRepository.findAll().getFirst().getTitle());
        assertEquals( TaskStatus.FINISHED, taskRepository.findAll().getFirst().getStatus());
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

    @Test
    void findByIdNotFound() {
        Executable executable = () -> taskService.findById(4L);
        assertThrows(TaskNotFoundException.class, executable);
    }

    @Test
    void findAll() {
        taskRepository.saveAll(List.of(
                Task.builder().title("title1").status(TaskStatus.PROCESSING).build(),
                Task.builder().title("title2").status(TaskStatus.NEW).build()
        ));
        assertEquals(2, taskService.findAll().size());
        assertEquals("title1", taskService.findAll().getFirst().getTitle());
    }

    @Test
    void deleteTask() {
        Task task = Task.builder().title("title1").status(TaskStatus.PROCESSING).build();
        taskRepository.save(task);
        taskService.delete(99L);
        assertFalse(taskRepository.findAll().isEmpty());
        taskService.delete(1L);
        assertTrue(taskRepository.findAll().isEmpty());
    }

    @Test
    void updateTask() {
        Task task = Task.builder().title("title1").status(TaskStatus.PROCESSING).build();
        taskRepository.save(task);
        TaskDto taskDto = TaskDto.builder().status("FINISHED").build();
        taskService.update(taskDto, 1L);
        assertTrue(taskRepository.findById(1L).isPresent());
        assertEquals(taskRepository.findById(1L).get().getStatus(), TaskStatus.FINISHED);
        Executable executable = () -> taskService.update(taskDto, 26L);
        assertThrows(TaskNotFoundException.class, executable);
    }
}
