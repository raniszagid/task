package com.example.task_app.service;

import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public void save(Task task) {
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public void update(TaskDto fresh, Task old) {
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

    public void delete(Task task) {
        taskRepository.delete(task);
    }
}
