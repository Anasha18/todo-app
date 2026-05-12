package com.example.todo.mapper;

import com.example.todo.controller.dto.ResponseTodoDto;
import com.example.todo.domain.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    ResponseTodoDto toDto(Todo todo);

    List<ResponseTodoDto> toDto(List<Todo> todo);
}
