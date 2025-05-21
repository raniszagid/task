package com.example.task_app.service;

import com.example.task_app.kafka.UpdateLogProducer;
import com.example.task_app.mapper.TaskMapper;
import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.model.enums.TaskStatus;
import com.example.task_app.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
