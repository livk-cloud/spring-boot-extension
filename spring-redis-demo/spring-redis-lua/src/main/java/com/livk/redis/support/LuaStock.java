package com.livk.redis.support;

import com.livk.common.redis.supprot.LivkRedisTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2022/3/7
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LuaStock {

    private final LivkRedisTemplate livkRedisTemplate;

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
