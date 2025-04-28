package com.example.task_app.mapper;

import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toEntity(TaskDto dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .userId(dto.getUserId())
                .build();
    }

    public TaskDto toDto(Task entity) {
        return TaskDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .userId(entity.getUserId())
                .build();
    }
}
