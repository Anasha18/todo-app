package com.example.todo.service;

import com.example.todo.controller.dto.RequestTodoDto;
import com.example.todo.controller.dto.ResponseTodoDto;
import com.example.todo.domain.Todo;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final RedisTemplate<String, Todo> redisTemplate;
    private final TodoMapper todoMapper;

    @Value("${app.ttl}")
    private int TTL;
    private static final String KEY = "todo:";

    @Transactional
    public ResponseTodoDto save(RequestTodoDto requestTodoDto) {
        Todo todoSaveToDb = todoRepository.save(requestTodoDto);
        return todoMapper.toDto(todoSaveToDb);
    }

    public ResponseTodoDto getById(Long id) {
        Todo todoFromCache = redisTemplate.opsForValue().get(KEY + id);

        if (todoFromCache != null) {
            log.info("find in cache by key {}", KEY + id);
            return todoMapper.toDto(todoFromCache);
        }

        Todo todoFromDb = todoRepository.getById(id);
        redisTemplate.opsForValue()
                .set(KEY + id, todoFromDb, TTL, TimeUnit.SECONDS);
        log.info("save in cache by key{}", KEY + id);

        return todoMapper.toDto(todoFromDb);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo with id " + id + " not found");
        }

        redisTemplate.delete(KEY + id);
        todoRepository.deleteById(id);
    }

    @Transactional
    public ResponseTodoDto updateById(RequestTodoDto dto, Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo with id " + id + " not found");
        }

        Todo todo = todoRepository.updateById(dto, id);
        redisTemplate.delete(KEY + id);
        return todoMapper.toDto(todo);
    }

    public List<ResponseTodoDto> getAllPagination(int page, int size) {
        int limit = size;
        int offset = page * size;

        List<Todo> todos = todoRepository.getAllPagination(limit, offset);

        return todoMapper.toDto(todos);
    }

    @Transactional
    public ResponseTodoDto completeById(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo with id " + id + " not found");
        }

        Todo todo = todoRepository.completeById(id);
        redisTemplate.delete(KEY + id);
        return todoMapper.toDto(todo);
    }
}
