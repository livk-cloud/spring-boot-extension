package com.livk.redisson.limit;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RateIntervalUnit;
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

    @Limiter(key = "livk:user", rate = 3, rateInterval = 10, rateIntervalUnit = RateIntervalUnit.SECONDS)
    @GetMapping
    public HttpEntity<Map<String, Object>> rate() {
        return ResponseEntity.ok(Map.of("username", "root", "password", "123456"));
    }

}
