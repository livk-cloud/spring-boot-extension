package com.livk.caffeine.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.livk.autoconfigure.redis.supprot.UniversalRedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

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

    private final UniversalRedisTemplate redisTemplate;

    private final Cache<String, Object> cache;

    @Override
    public void put(String key, Object proceed) {
        redisTemplate.opsForValue().set(key, proceed);
        cache.put(key, proceed);
    }

    @Override
    public void delete(String key) {
        cache.invalidate(key);
        redisTemplate.delete(key);
    }

    @Override
    public Object read(String key) {
        return cache.get(key, s -> redisTemplate.opsForValue().get(s));
    }

    @Override
    public void clear() {
        Set<String> keys = cache.asMap().keySet();
        cache.invalidateAll();
        redisTemplate.delete(keys);
    }
}
