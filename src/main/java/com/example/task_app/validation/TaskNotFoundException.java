package com.example.task_app.validation;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task with current id not found");
    }
}
