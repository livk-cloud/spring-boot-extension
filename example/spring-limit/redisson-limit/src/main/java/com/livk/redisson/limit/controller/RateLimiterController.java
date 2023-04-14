package com.livk.redisson.limit.controller;

import com.livk.autoconfigure.limit.annotation.Limit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * RateLimiterController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("limit")
@RequiredArgsConstructor
public class RateLimiterController {

    @Limit(key = "livk:user", rate = 2, rateInterval = 30)
    @GetMapping
    public HttpEntity<Map<String, Object>> rate() {
        return ResponseEntity.ok(Map.of("username", "root", "password", "123456"));
    }

}
