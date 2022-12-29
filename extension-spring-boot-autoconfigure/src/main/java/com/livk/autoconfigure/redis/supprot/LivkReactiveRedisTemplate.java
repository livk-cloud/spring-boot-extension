package com.livk.autoconfigure.redis.supprot;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * <p>
 * LivkReactiveRedisTemplate
 * </p>
 *
 * @author livk
 */
public class LivkReactiveRedisTemplate extends ReactiveRedisTemplate<String, Object> {

    public LivkReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                     RedisSerializationContext<String, Object> serializationContext) {
        this(connectionFactory, serializationContext, false);
    }

    public LivkReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                     RedisSerializationContext<String, Object> serializationContext, boolean exposeConnection) {
        super(connectionFactory, serializationContext, exposeConnection);
    }

}
