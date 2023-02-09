package com.livk.autoconfigure.redis.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.commons.spring.context.SpringContextHolder;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>
 * SerializerUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class SerializerUtils {

    /**
     * Json redis serializer.
     *
     * @param <T>    the type parameter
     * @param tClass the t class
     * @param mapper the mapper
     * @return the redis serializer
     */
    public <T> RedisSerializer<T> json(Class<T> tClass, ObjectMapper mapper) {
        return new Jackson2JsonRedisSerializer<>(mapper, tClass);
    }

    /**
     * Json redis serializer.
     *
     * @param <T>    the type parameter
     * @param tClass the t class
     * @return the redis serializer
     */
    public <T> RedisSerializer<T> json(Class<T> tClass) {
        ObjectMapper mapper = SpringContextHolder.getBeanProvider(ObjectMapper.class).getIfUnique();
        if (mapper == null) {
            mapper = JsonMapper.builder().build();
            mapper.registerModule(new JavaTimeModule());
        }
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return json(tClass, mapper);
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
