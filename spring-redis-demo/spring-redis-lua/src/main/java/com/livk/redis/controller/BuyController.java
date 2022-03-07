package com.livk.redis.controller;

import com.livk.redis.support.LuaStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("buy")
    public HttpEntity<String> buy() {
        return ResponseEntity.ok(luaStock.buy(1));
    }
}
