package com.example.task_app.service;

import com.example.task_app.kafka.UpdateLogProducer;
import com.example.task_app.mapper.TaskMapper;
import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.model.enums.TaskStatus;
import com.example.task_app.repository.TaskRepository;
import com.example.task_app.validation.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    private final TaskRepository taskRepositoryMock = Mockito.mock(TaskRepository.class);

    private final TaskMapper taskMapperMock = new TaskMapper();

    private final UpdateLogProducer updateLogProducerMock = Mockito.mock(UpdateLogProducer.class);

    @Test
    void findById() {
        Task task = Task.builder().id(1L).title("title").status(TaskStatus.NEW).build();
        when(taskRepositoryMock.findById(any())).thenReturn(Optional.of(task));
        TaskService taskService = new TaskService(taskRepositoryMock, taskMapperMock, updateLogProducerMock);
        TaskDto taskDto = taskService.findById(4L);

        assertNotNull(taskDto);
        assertEquals("title", taskDto.getTitle());
        assertEquals("NEW", taskDto.getStatus());
    }

    @Test
    void findByIdNotFound() {
        when(taskRepositoryMock.findById(any())).thenReturn(Optional.empty());
        TaskService taskService = new TaskService(taskRepositoryMock, taskMapperMock, updateLogProducerMock);
        Executable executable = () -> taskService.findById(4L);
        assertThrows(TaskNotFoundException.class, executable);
    }

    @Test
    void findAll() {
        when(taskRepositoryMock.findAll()).thenReturn(List.of(
                Task.builder().title("title1").status(TaskStatus.PROCESSING).build(),
                Task.builder().title("title2").status(TaskStatus.NEW).build()
        ));
        TaskService taskService = new TaskService(taskRepositoryMock, taskMapperMock, updateLogProducerMock);
        assertEquals(2, taskService.findAll().size());
        assertEquals("title1", taskService.findAll().getFirst().getTitle());
    }

    @Test
    void updateTask() {
        Task task = Task.builder().id(1L).title("title1").status(TaskStatus.NEW).build();
        when(taskRepositoryMock.findById(any())).thenReturn(Optional.of(task));
        TaskDto fresh = TaskDto.builder().status("PROCESSING").build();
        TaskService taskService = new TaskService(taskRepositoryMock, taskMapperMock, updateLogProducerMock);
        taskService.update(fresh, 1L);

        assertTrue(taskRepositoryMock.findById(1L).isPresent());
        assertEquals(taskRepositoryMock.findById(1L).get().getStatus(), TaskStatus.PROCESSING);
    }

    @Test
    void updateTaskNotFound() {
        when(taskRepositoryMock.findById(any())).thenReturn(Optional.empty());
        TaskService taskService = new TaskService(taskRepositoryMock, taskMapperMock, updateLogProducerMock);
        Executable executable = () -> taskService.update(TaskDto.builder().status("NEW").build(), 4L);
        assertThrows(TaskNotFoundException.class, executable);
    }
}
