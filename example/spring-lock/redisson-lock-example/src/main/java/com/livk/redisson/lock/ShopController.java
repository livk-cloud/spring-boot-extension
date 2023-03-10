package com.livk.redisson.lock;

import com.livk.autoconfigure.lock.annotation.OnLock;
import com.livk.autoconfigure.lock.constant.LockScope;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ShopController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("shop")
public class ShopController {

    private final HashOperations<Object, Object, Object> forHash;
    private final RedisTemplate<Object, Object> redisTemplate;

    public ShopController(RedisTemplate<Object, Object> redisTemplate) {
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        this.forHash = redisTemplate.opsForHash();
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        redisTemplate.delete("shop");
        forHash.put("shop", "num", 500);
    }

    @PostMapping("/buy/distributed")
    @OnLock(key = "shop", scope = LockScope.DISTRIBUTED_LOCK)
    public HttpEntity<Map<String, Object>> buyDistributed(@RequestParam(defaultValue = "2") Integer count) {
        RedisScript<Long> redisScript = RedisScript.of(new ClassPathResource("script/buy.lua"), Long.class);
        Long result = redisTemplate.execute(redisScript, RedisSerializer.string(), new GenericToStringSerializer<>(Long.class), List.of("shop", "num", "buySucCount", "buyCount"), String.valueOf(count));
        if (result != null && result == 1) {
            return ResponseEntity.ok(Map.of("code", "200", "msg", "购买成功，数量：" + count));
        }
        return ResponseEntity.ok(Map.of("code", "500", "msg", "数量超出库存！"));
    }

    @GetMapping("result")
    public HttpEntity<Map<String, Object>> result() {
        Map<Object, Object> distributed = forHash.entries("shop");
        return ResponseEntity.ok(Map.of("redisson", distributed));
    }
}
