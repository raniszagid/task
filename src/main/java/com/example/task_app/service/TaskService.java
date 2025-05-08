package com.example.task_app.service;

import com.example.task_app.mapper.TaskMapper;
import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
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

    public void save(Task task) {
        taskRepository.save(task);
    }

    public List<TaskDto> findAll() {
        return taskRepository.findAll().stream().map(taskMapper::toDto).toList();
    }

    public TaskDto findById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return taskMapper.toDto(optionalTask.orElseThrow(TaskNotFoundException::new));
    }

    public void update(TaskDto fresh, Long id) {
        Optional<Task> oldOptional = taskRepository.findById(id);
        if (oldOptional.isPresent()) {
            Task old = oldOptional.get();
            if (fresh.getTitle() != null) {
                old.setTitle(fresh.getTitle());
            }
            if (fresh.getDescription() != null) {
                old.setDescription(fresh.getDescription());
            }
            if (fresh.getUserId() != null) {
                old.setUserId(fresh.getUserId());
            }
            taskRepository.save(old);
        }
        else {
            throw new TaskNotFoundException();
        }
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
