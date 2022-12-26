package com.livk.caffeine.controller;

import com.livk.caffeine.annotation.DoubleCache;
import com.livk.caffeine.enums.CacheType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * <p>
 * CacheController
 * </p>
 *
 * @author livk
 * @date 2022/12/26
 */
@RestController
@RequestMapping("cache")
public class CacheController {

    @DoubleCache(cacheName = "cache", key = "local")
    @GetMapping
    public String get() {
        return UUID.randomUUID().toString();
    }

    @DoubleCache(cacheName = "cache", key = "local", type = CacheType.PUT)
    @PostMapping
    public String put() {
        return UUID.randomUUID().toString();
    }

    @DoubleCache(cacheName = "cache", key = "local", type = CacheType.DELETE)
    @DeleteMapping
    public String delete() {
        return "over";
    }
}
