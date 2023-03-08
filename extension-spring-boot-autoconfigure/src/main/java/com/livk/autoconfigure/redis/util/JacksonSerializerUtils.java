package com.livk.autoconfigure.redis.util;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * The type Jackson serializer utils.
 */
@UtilityClass
public class JacksonSerializerUtils {

    /**
     * Json redis serializer.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @param mapper      the mapper
     * @return the redis serializer
     */
    public <T> RedisSerializer<T> json(Class<T> targetClass, ObjectMapper mapper) {
        return new Jackson2JsonRedisSerializer<>(mapper, targetClass);
    }

    /**
     * Json redis serializer.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @return the redis serializer
     */
    public <T> RedisSerializer<T> json(Class<T> targetClass) {
        return json(targetClass, new JavaTimeModule());
    }

    /**
     * Json redis serializer.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @param modules     the modules
     * @return the redis serializer
     */
    public <T> RedisSerializer<T> json(Class<T> targetClass, Module... modules) {
        ObjectMapper mapper = JsonMapper.builder().build();
        mapper.registerModules(modules);
        return json(targetClass, mapper);
    }

    /**
     * Json redis serializer.
     *
     * @return the redis serializer
     */
    public RedisSerializer<Object> json() {
        return json(Object.class);
    }

}
