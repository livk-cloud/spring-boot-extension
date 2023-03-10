package com.livk.caffeine.handler;

/**
 * <p>
 * CacheReadHandler
 * </p>
 *
 * @author livk
 */
public interface CacheReadHandler<T> {

    T read(String key);

}
