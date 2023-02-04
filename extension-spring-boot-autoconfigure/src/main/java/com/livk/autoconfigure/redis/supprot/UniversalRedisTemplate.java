package com.livk.autoconfigure.redis.supprot;

import com.livk.autoconfigure.redis.util.SerializerUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * The type Universal redis template.
 *
 * @author livk
 */
public class UniversalRedisTemplate extends RedisTemplate<String, Object> {

    /**
     * Instantiates a new Universal redis template.
     */
    public UniversalRedisTemplate() {
        RedisSerializer<Object> serializer = SerializerUtils.json();
        this.setKeySerializer(RedisSerializer.string());
        this.setHashKeySerializer(RedisSerializer.string());
        this.setValueSerializer(serializer);
        this.setHashValueSerializer(serializer);
    }

    /**
     * Instantiates a new Universal redis template.
     *
     * @param redisConnectionFactory the redis connection factory
     */
    public UniversalRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        this();
        this.setConnectionFactory(redisConnectionFactory);
        this.afterPropertiesSet();
    }

}
