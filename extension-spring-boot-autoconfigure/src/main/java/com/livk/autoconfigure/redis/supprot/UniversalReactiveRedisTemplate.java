package com.livk.autoconfigure.redis.supprot;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * The type Universal reactive redis template.
 *
 * @author livk
 */
public class UniversalReactiveRedisTemplate extends ReactiveRedisTemplate<String, Object> {

    /**
     * Instantiates a new Universal reactive redis template.
     *
     * @param connectionFactory    the connection factory
     * @param serializationContext the serialization context
     */
    public UniversalReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                          RedisSerializationContext<String, Object> serializationContext) {
        this(connectionFactory, serializationContext, false);
    }

    /**
     * Instantiates a new Universal reactive redis template.
     *
     * @param connectionFactory    the connection factory
     * @param serializationContext the serialization context
     * @param exposeConnection     the expose connection
     */
    public UniversalReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                          RedisSerializationContext<String, Object> serializationContext, boolean exposeConnection) {
        super(connectionFactory, serializationContext, exposeConnection);
    }

}
