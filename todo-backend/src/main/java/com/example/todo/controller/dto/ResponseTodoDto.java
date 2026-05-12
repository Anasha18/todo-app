package com.example.todo.controller.dto;

import java.time.LocalDateTime;

public record ResponseTodoDto(
        Long id,
        String name,
        String description,
        boolean isDone,
        LocalDateTime createdAt
) {
}
