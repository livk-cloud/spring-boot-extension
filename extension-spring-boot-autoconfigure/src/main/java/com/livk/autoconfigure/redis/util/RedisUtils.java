package com.livk.autoconfigure.redis.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * RedisUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class RedisUtils {

    /**
     * scan命令,会获取到所有匹配的key，直到关闭
     *
     * @param redisTemplate redis模板
     * @param pattern       key匹配关系
     * @param limit         每次扫描个数
     * @return {@link Cursor}
     */
    public Cursor<String> scan(RedisTemplate<String, ?> redisTemplate, String pattern, long limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
        return redisTemplate.executeWithStickyConnection(connection -> new ConvertingCursor<>(
                connection.keyCommands().scan(options),
                source -> (String) redisTemplate.getKeySerializer().deserialize(source)));
    }

    /**
     * scan命令,获取匹配的key
     *
     * @param redisTemplate redis模板
     * @param pattern       key匹配关系
     * @param limit         每次扫描个数
     * @param returnSize    返回的数量
     * @return {@link Set}
     */
    public Set<String> scan(RedisTemplate<String, ?> redisTemplate, String pattern, long limit, int returnSize) {
        Set<String> keys = new HashSet<>(returnSize);
        try (Cursor<String> scan = scan(redisTemplate, pattern, limit)) {
            while (scan.hasNext() && keys.size() < returnSize) {
                keys.add(scan.next());
            }
        }
        return keys;
    }

}
