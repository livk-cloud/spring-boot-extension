package com.livk.caffeine.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.livk.autoconfigure.redis.supprot.LivkRedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * CacheHandlerAdapter
 * </p>
 *
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class CacheHandlerAdapter implements CacheHandler<Object> {

    private final LivkRedisTemplate livkRedisTemplate;

    private final Cache<String, Object> cache;

    @Override
    public void put(String key, Object proceed, long timeout) {
        livkRedisTemplate.opsForValue().set(key, proceed, timeout, TimeUnit.SECONDS);
        cache.put(key, proceed);
    }

    @Override
    public void delete(String key) {
        cache.invalidate(key);
        livkRedisTemplate.delete(key);
    }

    @Override
    public Object read(String key) {
        return cache.get(key, s -> livkRedisTemplate.opsForValue().get(s));
    }

}
