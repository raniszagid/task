package com.example.task_app.controller;

import com.example.task_app.aop.annotation.BeforeMethodExecution;
import com.example.task_app.aop.annotation.ExecutionDuration;
import com.example.task_app.aop.annotation.NotFoundErrorCatcher;
import com.example.task_app.aop.annotation.TaskReturnAnnotation;
import com.example.task_app.mapper.TaskMapper;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @ExecutionDuration
    @GetMapping
    public List<TaskDto> getTasks() {
        return taskService.findAll();
    }

    @TaskReturnAnnotation
    @NotFoundErrorCatcher
    @GetMapping("/{id}")
    public TaskDto getCertainTask(@PathVariable("id") Long id) {
        return taskService.findById(id);
    }

    @BeforeMethodExecution
    @PostMapping
    public void create(@RequestBody TaskDto taskDto) {
        taskService.save(taskMapper.toEntity(taskDto));
    }

    @PutMapping("/{id}")
    @NotFoundErrorCatcher
    public void update(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
        taskService.update(taskDto, id);
    }

    @DeleteMapping("/{id}")
    @NotFoundErrorCatcher
    public void delete(@PathVariable("id") Long id) {
        taskService.delete(id);
    }
}
