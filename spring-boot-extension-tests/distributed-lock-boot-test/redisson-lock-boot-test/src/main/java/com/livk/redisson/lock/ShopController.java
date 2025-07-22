/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.redisson.lock;

import com.livk.context.lock.annotation.DistLock;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
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
	@DistLock(key = "shop:lock")
	public HttpEntity<Map<String, Object>> buy(@RequestParam(defaultValue = "2") Integer count) {
		RedisScript<Long> redisScript = RedisScript.of(new ClassPathResource("script/buy.lua"), Long.class);
		Long result = redisTemplate.execute(redisScript, RedisSerializer.string(),
				new GenericToStringSerializer<>(Long.class), List.of("shop", "num", "buySucCount", "buyCount"),
				String.valueOf(count));
		if (result == 1) {
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
