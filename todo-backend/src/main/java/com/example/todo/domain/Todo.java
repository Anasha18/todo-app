package com.example.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    private Long id;
    private String name;
    private String description;
    private Boolean isDone;
    private LocalDateTime createdAt;
}
