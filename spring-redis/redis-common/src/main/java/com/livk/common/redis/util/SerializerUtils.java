package com.livk.common.redis.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.support.SpringContextHolder;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>
 * SerializerUtils
 * </p>
 *
 * @author livk
 * @date 2022/4/1
 */
@UtilityClass
public class SerializerUtils {

    public <T> RedisSerializer<T> json(Class<T> tClass, ObjectMapper mapper) {
        return new Jackson2JsonRedisSerializer<>(mapper, tClass);
    }

    public <T> RedisSerializer<T> json(Class<T> tClass) {
        ObjectMapper mapper = SpringContextHolder.getBeanProvider(ObjectMapper.class).getIfUnique();
        if (mapper == null) {
            mapper = JsonMapper.builder().build();
            mapper.registerModule(new JavaTimeModule());
        }
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return json(tClass, mapper);
    }

    public RedisSerializer<Object> json() {
        return json(Object.class);
    }

}
