package com.example.todo.repository;

import com.example.todo.controller.dto.RequestTodoDto;
import com.example.todo.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class TodoRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SAVE = """
            insert into todos (name, description)
            values (:name, :description)
            returning id, name, description, is_done, created_at
            """.trim();

    private static final String GET_BY_ID = """
            select id, name, description, is_done, created_at
            from todos
            where id = :id
            order by name
            """.trim();

    private static final String GET_ALL_BY_PAGE = """
            select id, name, description, is_done, created_at
            from todos
            order by name asc
            limit :limit offset :offset
            """.trim();

    private static final String DELETE_BY_ID = """
            delete from todos where id = :id
            """.trim();

    private static final String EXISTS_BY_ID = """
            select exists (select * from todos where id = :id)
            """.trim();

    private static final String UPDATE_BY_ID = """
            update todos set name = :name, description = :description
            where id = :id
            returning id, name, description, is_done, created_at
            """.trim();

    private static final String COMPLETED_BY_ID = """
            update todos set is_done = true
            where id = :id
            returning id, name, description, is_done, created_at
            """;

    public Todo save(RequestTodoDto dto) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", dto.name())
                .addValue("description", dto.description());
        return jdbcTemplate.queryForObject(SAVE, params, mapper);
    }

    public Todo getById(Long id) {
        return jdbcTemplate
                .query(GET_BY_ID, Map.of("id", id), mapper)
                .stream()
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Todo not found with id: " + id)
                );
    }

    public List<Todo> getAllPagination(int limit, int offset) {
        return jdbcTemplate.query(GET_ALL_BY_PAGE,
                Map.of("limit", limit, "offset", offset),
                mapper);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, Map.of("id", id));
    }

    public Todo updateById(RequestTodoDto dto, Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", dto.name())
                .addValue("description", dto.description())
                .addValue("id", id);
        return jdbcTemplate.queryForObject(UPDATE_BY_ID, params, mapper);
    }

    public Todo completeById(Long id) {
        return jdbcTemplate.queryForObject(COMPLETED_BY_ID, Map.of("id", id), mapper);
    }

    public Boolean existsById(Long id) {
        return jdbcTemplate.queryForObject(EXISTS_BY_ID, Map.of("id", id), Boolean.class);
    }

    private final RowMapper<Todo> mapper = (rs, _) -> Todo.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .isDone(rs.getBoolean("is_done"))
            .createdAt(rs.getTimestamp("created_at")
                    .toLocalDateTime()
            )
            .build();
}
