package com.example.todo.controller;

import com.example.todo.controller.dto.RequestTodoDto;
import com.example.todo.controller.dto.ResponseTodoDto;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseTodoDto save(
            @Valid @RequestBody RequestTodoDto dto
    ) {
        return todoService.save(dto);
    }

    @GetMapping("/{id}")
    public ResponseTodoDto getById(@PathVariable Long id) {
        return todoService.getById(id);
    }

    @GetMapping
    public List<ResponseTodoDto> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size
    ) {
        return todoService.getAllPagination(page, size);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        todoService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseTodoDto update(
            @Valid @RequestBody RequestTodoDto dto,
            @PathVariable Long id
    ) {
        return todoService.updateById(dto, id);
    }

    @PutMapping("/complete/{id}")
    public ResponseTodoDto completeById(@PathVariable Long id) {
        return todoService.completeById(id);
    }
}

