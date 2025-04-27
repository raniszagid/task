package com.example.task_app.controller;

import com.example.task_app.aop.annotation.ExecutionDuration;
import com.example.task_app.mapper.TaskMapper;
import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @ExecutionDuration
    @GetMapping
    public List<TaskDto> getTasks() {
        return taskService.findAll().stream().map(taskMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getCertainTask(@PathVariable("id") Long id) {
        Optional<Task> optionalTask = taskService.findById(id);
        return optionalTask.map(task -> ResponseEntity.ok(taskMapper.toDto(task)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void create(@RequestBody TaskDto taskDto) {
        taskService.save(taskMapper.toEntity(taskDto));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
        Task task = taskService.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        taskService.update(taskDto, task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        Task task = taskService.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        taskService.delete(task);
    }
}
