package com.livk.local.lock;

import com.livk.lock.annotation.OnLock;
import com.livk.lock.constant.LockScope;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2022/9/29
 */
@Slf4j
@RestController
@RequestMapping("shop")
public class ShopController {

    private Integer num = 500;

    private int buyCount = 0;

    private int buySucCount = 0;

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
        forHash.put("shop", "num", 500);
    }

    @PostMapping("/buy/local")
    @OnLock(key = "shop", scope = LockScope.STANDALONE_LOCK)
    public HttpEntity<Map<String, Object>> buyLocal(@RequestParam Integer count) {
        buyCount++;
        if (num >= count) {
            num -= count;
            buySucCount++;
            return ResponseEntity.ok(Map.of("code", "200", "msg", "购买成功，数量：" + count));
        } else {
            return ResponseEntity.ok(Map.of("code", "500", "msg", "数量超出库存！"));
        }
    }

    @PostMapping("/buy/distributed")
    @OnLock(key = "shop", scope = LockScope.DISTRIBUTED_LOCK)
    public HttpEntity<Map<String, Object>> buyDistributed(@RequestParam Integer count) {
        RedisScript<Long> redisScript = RedisScript.of(new ClassPathResource("script/buy.lua"), Long.class);
        Long result = redisTemplate.execute(redisScript, RedisSerializer.string(), new GenericToStringSerializer<>(Long.class), List.of("shop", "num", "buySucCount", "buyCount"), String.valueOf(count));
        if (result != null && result == 1) {
            return ResponseEntity.ok(Map.of("code", "200", "msg", "购买成功，数量：" + count));
        }
        return ResponseEntity.ok(Map.of("code", "500", "msg", "数量超出库存！"));
    }

    @PostMapping("reset")
    public void add() {
        num = 500;
        buyCount = 0;
        buySucCount = 0;
    }

    @GetMapping("result")
    public Map<String, Integer> result() {
        return Map.of("num", num, "buyCount", buyCount, "buySucCount", buySucCount);
    }
}
