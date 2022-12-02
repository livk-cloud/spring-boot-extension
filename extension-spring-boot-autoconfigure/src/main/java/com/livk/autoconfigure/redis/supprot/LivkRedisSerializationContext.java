package com.livk.autoconfigure.redis.supprot;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LivkRedisSerializationContext<T> implements RedisSerializationContext<String, T> {

    private final RedisSerializer<T> serializer;

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
        return (SerializationPair<HK>) RedisSerializationContext.SerializationPair
                .fromSerializer(RedisSerializer.string());
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
