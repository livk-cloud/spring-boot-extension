package com.livk.caffeine.config;

import com.livk.caffeine.handler.CacheHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class RedisCaffeineManager implements CacheManager {

    private final CacheHandler<Object> adapter;

    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        return (cache != null) ? cache : this.cacheMap.computeIfAbsent(name, this::createCache);
    }

    private Cache createCache(String name) {
        return new RedisCaffeineCache(name, adapter, true);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }
}
