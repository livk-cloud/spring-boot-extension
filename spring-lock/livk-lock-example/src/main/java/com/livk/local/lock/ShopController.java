package com.livk.local.lock;

import com.livk.lock.annotation.OnLock;
import com.livk.lock.constant.LockScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/buy/local")
    @OnLock(key = "shop", scope = LockScope.STANDALONE_LOCK)
    public HttpEntity<Map<String, Object>> buyLocal(@RequestParam Integer count) {
        return buy(count);
    }

    @PostMapping("/buy/distributed")
    @OnLock(key = "shop", scope = LockScope.DISTRIBUTED_LOCK)
    public HttpEntity<Map<String, Object>> buyDistributed(@RequestParam Integer count) {
        return buy(count);
    }

    private HttpEntity<Map<String, Object>> buy(Integer count) {
        buyCount++;
        if (num >= count) {
            num -= count;
            buySucCount++;
            return ResponseEntity.ok(Map.of("code", "200", "msg", "购买成功，数量：" + count));
        } else {
            return ResponseEntity.ok(Map.of("code", "500", "msg", "数量超出库存！"));
        }
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
