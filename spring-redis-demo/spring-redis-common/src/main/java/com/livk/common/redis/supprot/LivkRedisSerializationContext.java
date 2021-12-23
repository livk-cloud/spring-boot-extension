package com.livk.common.redis.supprot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import reactor.util.annotation.NonNull;

/**
 * <p>
 * LivkRedisSerializationContext
 * </p>
 *
 * @author livk
 * @date 2021/12/23
 */
public class LivkRedisSerializationContext implements RedisSerializationContext<String, Object> {

    private final Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

    public LivkRedisSerializationContext(){
        var mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        serializer.setObjectMapper(mapper);
    }

    @NonNull
    @Override
    public SerializationPair<String> getKeySerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
    }

    @NonNull
    @Override
    public SerializationPair<Object> getValueSerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(serializer);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public SerializationPair<String> getHashKeySerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public SerializationPair<Object> getHashValueSerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(serializer);
    }

    @NonNull
    @Override
    public SerializationPair<String> getStringSerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
    }
}
