package com.example.task_app.service;

import com.example.task_app.kafka.UpdateLogProducer;
import com.example.task_app.mapper.TaskMapper;
import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.model.enums.TaskStatus;
import com.example.task_app.repository.TaskRepository;
import com.example.task_app.validation.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UpdateLogProducer updateLogProducer;

    public void save(Task task) {
        taskRepository.save(task);
    }

    public List<TaskDto> findAll() {
        return taskRepository.findAll().stream().map(taskMapper::toDto).toList();
    }

    public TaskDto findById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        return taskMapper.toDto(task);
    }

    public void update(TaskDto fresh, Long id) {
        Task old = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
            if (fresh.getTitle() != null) {
                old.setTitle(fresh.getTitle());
            }
            if (fresh.getDescription() != null) {
                old.setDescription(fresh.getDescription());
            }
            if (fresh.getUserId() != null) {
                old.setUserId(fresh.getUserId());
            }
            if (fresh.getStatus() != null) {
                old.setStatus(TaskStatus.valueOf(fresh.getStatus()));
            }
            taskRepository.save(old);
            updateLogProducer.send(fresh, id);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
