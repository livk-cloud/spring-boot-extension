package com.livk.caffeine.handler;

/**
 * <p>
 * CacheWriteHandler
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
public interface CacheWriteHandler<T> {
    void put(String key, T value, long timeout);

    void delete(String key);
}
