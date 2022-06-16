package com.livk.common.redis.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

/**
 * <p>
 * RedisUtils
 * </p>
 *
 * @author livk
 * @date 2022/5/24
 */
@UtilityClass
public class RedisUtils {

	public Cursor<Object> scan(RedisTemplate<String, ?> redisTemplate, String pattern, int limit) {
		var options = ScanOptions.scanOptions().match(pattern).count(limit).build();
		return redisTemplate.executeWithStickyConnection(connection -> new ConvertingCursor<>(
				connection.keyCommands().scan(options), redisTemplate.getKeySerializer()::deserialize));
	}

}
