package com.livk.caffeine.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.livk.common.redis.supprot.LivkRedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * CacheHandlerAdapter
 * </p>
 *
 * @author livk
 * @date 2022/3/30
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
		var cacheObj = cache.getIfPresent(key);
		if (cacheObj == null) {
			cacheObj = livkRedisTemplate.opsForValue().get(key);
		}
		return cacheObj;
	}

}
