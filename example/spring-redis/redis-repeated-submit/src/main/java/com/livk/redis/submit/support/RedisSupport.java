package com.livk.redis.submit.support;

import com.livk.autoconfigure.redis.supprot.UniversalRedisTemplate;
import com.livk.commons.spring.context.SpringContextHolder;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * The type Redis support.
 *
 * @author livk
 */
@UtilityClass
public class RedisSupport {

    private static final ValueOperations<String, Object> OPERATIONS;

    private static final UniversalRedisTemplate REDIS_TEMPLATE;

    static {
        REDIS_TEMPLATE = SpringContextHolder.getBean(UniversalRedisTemplate.class);
        OPERATIONS = REDIS_TEMPLATE.opsForValue();
    }


    /**
     * 写入缓存设置时效时间
     *
     * @param key        the key
     * @param value      the value
     * @param expireTime the expire time
     */
    public void setEx(String key, Object value, Long expireTime) {
        OPERATIONS.set(key, value, expireTime, TimeUnit.SECONDS);
    }


    /**
     * 判断缓存中是否有对应的value
     *
     * @param key the key
     * @return boolean boolean
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(REDIS_TEMPLATE.hasKey(key));
    }
}
