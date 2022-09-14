package com.livk.redis.controller;

import com.livk.common.redis.supprot.LivkRedisTemplate;
import com.livk.redis.support.LuaStock;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * BuyController
 * </p>
 *
 * @author livk
 * @date 2022/3/7
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BuyController {

    private final LuaStock luaStock;

    private final LivkRedisTemplate livkRedisTemplate;

    @PostConstruct
    public void init() {
        if (Boolean.TRUE.equals(livkRedisTemplate.hasKey("livk"))) {
            livkRedisTemplate.delete("livk");
        }
        livkRedisTemplate.opsForValue().set("livk", 1);
    }

    @PostMapping("buy")
    public HttpEntity<String> buy() {
        return ResponseEntity.ok(luaStock.buy(1));
    }

    @PostMapping("put")
    public void put() {
        ValueOperations<String, Object> value = livkRedisTemplate.opsForValue();
        if ((Integer) value.get("livk") > 0) {
            value.decrement("livk");
        }
    }

}
