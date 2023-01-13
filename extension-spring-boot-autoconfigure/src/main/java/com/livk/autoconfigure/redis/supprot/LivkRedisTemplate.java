package com.livk.autoconfigure.redis.supprot;

import com.livk.autoconfigure.redis.util.SerializerUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>
 * LivkRedisTemplate
 * </p>
 *
 * @author livk
 */
public class LivkRedisTemplate extends RedisTemplate<String, Object> {

    /**
     * Instantiates a new Livk redis template.
     */
    public LivkRedisTemplate() {
        RedisSerializer<Object> serializer = SerializerUtils.json();
        this.setKeySerializer(RedisSerializer.string());
        this.setHashKeySerializer(RedisSerializer.string());
        this.setValueSerializer(serializer);
        this.setHashValueSerializer(serializer);
    }

    /**
     * Instantiates a new Livk redis template.
     *
     * @param redisConnectionFactory the redis connection factory
     */
    public LivkRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        this();
        this.setConnectionFactory(redisConnectionFactory);
        this.afterPropertiesSet();
    }

}
