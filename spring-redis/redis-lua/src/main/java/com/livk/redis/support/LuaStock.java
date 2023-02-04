package com.livk.redis.support;

import com.livk.autoconfigure.redis.supprot.UniversalRedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * LuaStock
 * </p>
 *
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class LuaStock {

    private final UniversalRedisTemplate livkRedisTemplate;

    public String buy(Integer num) {
        RedisScript<Long> redisScript = RedisScript.of(new ClassPathResource("good.lua"), Long.class);
        Long result = livkRedisTemplate.execute(redisScript, List.of("stock"), num);
        Assert.notNull(result, "RedisScript Result is Null!");
        if (0 == result) {
            return "没了";
        } else if (2 == result) {
            return "抢到了";
        }
        return "";
    }

}
