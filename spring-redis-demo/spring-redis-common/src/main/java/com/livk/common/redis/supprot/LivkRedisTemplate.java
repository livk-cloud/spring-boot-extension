package com.livk.common.redis.supprot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 * LivkRedisTemplate
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
public class LivkRedisTemplate extends RedisTemplate<String, Object> {

    public LivkRedisTemplate() {
        var serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        var mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        serializer.setObjectMapper(mapper);
        var stringRedisSerializer = new StringRedisSerializer();
        this.setKeySerializer(stringRedisSerializer);
        this.setHashKeySerializer(stringRedisSerializer);
        this.setValueSerializer(serializer);
        this.setHashValueSerializer(serializer);
    }

    public LivkRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        this();
        this.setConnectionFactory(redisConnectionFactory);
        this.afterPropertiesSet();
    }

}
