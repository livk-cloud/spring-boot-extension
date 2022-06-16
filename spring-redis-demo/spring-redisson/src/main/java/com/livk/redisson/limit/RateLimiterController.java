package com.livk.redisson.limit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2022/6/13
 */
@Slf4j
@RestController
@RequestMapping("limit")
@RequiredArgsConstructor
public class RateLimiterController {

	@Limiter(key = "livk:user", rate = 3, rateInterval = 10)
	@GetMapping
	public HttpEntity<Map<String, Object>> rate() {
		return ResponseEntity.ok(Map.of("username", "root", "password", "123456"));
	}

}
