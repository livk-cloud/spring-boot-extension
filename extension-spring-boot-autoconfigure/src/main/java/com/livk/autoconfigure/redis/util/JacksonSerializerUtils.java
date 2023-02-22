package com.livk.autoconfigure.redis.util;

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
     * @param <T>             the type parameter
     * @param targetClass     the t class
     * @param useSpringMapper the is use spring mapper
     * @return the redis serializer
     */
    public <T> RedisSerializer<T> json(Class<T> targetClass, boolean useSpringMapper) {
        ObjectMapper mapper = useSpringMapper ?
                SpringContextHolder.getBean(ObjectMapper.class) :
                JsonMapper.builder().build();
        mapper.registerModule(new JavaTimeModule());
        return json(targetClass, mapper);
    }

    /**
     * Json redis serializer.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @return the redis serializer
     */
    public <T> RedisSerializer<T> json(Class<T> targetClass) {
        return json(targetClass, false);
    }

    /**
     * Json redis serializer.
     *
     * @return the redis serializer
     */
    public RedisSerializer<Object> json() {
        return json(Object.class);
    }

    /**
     * Spring json redis serializer.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @return the redis serializer
     */
    public <T> RedisSerializer<T> springJson(Class<T> targetClass) {
        return json(targetClass, true);
    }

    /**
     * Spring json redis serializer.
     *
     * @return the redis serializer
     */
    public RedisSerializer<Object> springJson() {
        return json(Object.class);
    }

}
