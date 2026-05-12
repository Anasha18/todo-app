package com.example.todo.config;

import com.example.todo.domain.Todo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Todo> redisTemplate(
            RedisConnectionFactory factory,
            ObjectMapper objectMapper
    ) {
        RedisTemplate<String, Todo> template = new RedisTemplate<>();

        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JacksonJsonRedisSerializer<>(objectMapper, Todo.class));
        template.setHashValueSerializer(new JacksonJsonRedisSerializer<>(objectMapper, Todo.class));

        template.afterPropertiesSet();
        return template;
    }
}
