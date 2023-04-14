package com.livk.caffeine.config;

import com.livk.caffeine.handler.CacheHandler;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author livk
 */
public class RedisCaffeineCache extends AbstractValueAdaptingCache {

    private final String name;

    private final CacheHandler<Object> cacheHandler;

    protected RedisCaffeineCache(String name, CacheHandler<Object> cacheHandler, boolean allowNullValues) {
        super(allowNullValues);
        this.name = name;
        this.cacheHandler = cacheHandler;
    }

    @Override
    protected Object lookup(Object key) {
        return cacheHandler.read(key.toString());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public CacheHandler<Object> getNativeCache() {
        return cacheHandler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return (T) fromStoreValue(this.cacheHandler.readAndPut(key.toString(),
                supplier(valueLoader).get()));
    }

    @Override
    public void put(Object key, Object value) {
        this.cacheHandler.put(key.toString(), toStoreValue(value));
    }

    @Override
    public void evict(Object key) {
        this.cacheHandler.delete(key.toString());
    }

    @Override
    public void clear() {
        this.cacheHandler.clear();
    }

    private <V> Supplier<V> supplier(Callable<V> callable) {
        return () -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
