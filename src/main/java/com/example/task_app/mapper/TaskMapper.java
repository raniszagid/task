package com.example.task_app.mapper;

import com.example.task_app.model.Task;
import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.model.enums.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toEntity(TaskDto dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .userId(dto.getUserId())
                .status(TaskStatus.valueOf(dto.getStatus()))
                .build();
    }

    public TaskDto toDto(Task entity) {
        return TaskDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .userId(entity.getUserId())
                .status(entity.getStatus().name())
                .build();
    }
}
