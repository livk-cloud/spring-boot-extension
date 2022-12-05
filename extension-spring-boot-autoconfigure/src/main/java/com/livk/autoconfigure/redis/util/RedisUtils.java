package com.livk.autoconfigure.redis.util;

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
 * 
 */
@UtilityClass
public class RedisUtils {

    public Cursor<Object> scan(RedisTemplate<String, ?> redisTemplate, String pattern, int limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
        return redisTemplate.executeWithStickyConnection(connection -> new ConvertingCursor<>(
                connection.keyCommands().scan(options), redisTemplate.getKeySerializer()::deserialize));
    }

}
