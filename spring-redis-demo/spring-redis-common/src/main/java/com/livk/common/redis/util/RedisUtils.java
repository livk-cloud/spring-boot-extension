package com.livk.common.redis.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.redis.core.*;

/**
 * <p>
 * RedisUtils
 * </p>
 *
 * @author livk
 * @date 2022/4/18
 */
@UtilityClass
public class RedisUtils {

	public Cursor<Object> scan(RedisTemplate<String, ?> redisTemplate, String pattern, int limit) {
		var options = ScanOptions.scanOptions().match(pattern).count(limit).build();
		return redisTemplate.executeWithStickyConnection(connection -> new ConvertingCursor<>(connection.scan(options),
				redisTemplate.getKeySerializer()::deserialize));
	}

}
