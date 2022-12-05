package com.livk.caffeine.handler;

/**
 * <p>
 * CacheWriteHandler
 * </p>
 *
 * @author livk
 */
public interface CacheWriteHandler<T> {

    void put(String key, T value, long timeout);

    void delete(String key);

}
