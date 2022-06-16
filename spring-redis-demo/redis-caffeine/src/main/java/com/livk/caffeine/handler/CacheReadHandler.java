package com.livk.caffeine.handler;

/**
 * <p>
 * CacheReadHandler
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
public interface CacheReadHandler<T> {

    T read(String key);

}
