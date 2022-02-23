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
public record LivkRedisSerializationContext<T>(
        Jackson2JsonRedisSerializer<T> serializer) implements RedisSerializationContext<String, T> {

    public LivkRedisSerializationContext(Jackson2JsonRedisSerializer<T> serializer) {
        this.serializer = serializer;
        var mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        this.serializer.setObjectMapper(mapper);
    }

    @NonNull
    @Override
    public SerializationPair<String> getKeySerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
    }

    @NonNull
    @Override
    public SerializationPair<T> getValueSerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(serializer);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <HK> SerializationPair<HK> getHashKeySerializationPair() {
        return (SerializationPair<HK>) RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <HV> SerializationPair<HV> getHashValueSerializationPair() {
        return (SerializationPair<HV>) RedisSerializationContext.SerializationPair.fromSerializer(serializer);
    }

    @NonNull
    @Override
    public SerializationPair<String> getStringSerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
    }
}
