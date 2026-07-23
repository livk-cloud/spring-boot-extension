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

import com.livk.commons.io.ResourceScanner;
import com.livk.context.lock.annotation.DistLock;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RMap;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.CompositeCodec;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author livk
 */
@RestController
@RequestMapping("shop")
public class ShopController {

	private final RedissonClient redissonClient;

	private final RMap<String, Object> shop;

	public ShopController(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
		Codec mapCodec = new CompositeCodec(StringCodec.INSTANCE, StringCodec.INSTANCE,
				redissonClient.getConfig().getCodec());
		this.shop = redissonClient.getMap("shop", mapCodec);
	}

	@PostConstruct
	public void init() {
		shop.delete();
		shop.put("num", 500);
	}

	@PostMapping("/buy/distributed")
	@DistLock(key = "shop:lock")
	public HttpEntity<Map<String, Object>> buy(@RequestParam(defaultValue = "2") Integer count) throws IOException {
		String luaScript = ResourceScanner
			.getResource(org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX + "script/buy.lua")
			.getContentAsString(StandardCharsets.UTF_8);
		List<Object> keys = List.of("shop", "num", "buySucCount", "buyCount");
		RScript script = redissonClient.getScript();

		Long result = script.eval(RScript.Mode.READ_WRITE, luaScript, RScript.ReturnType.LONG, keys, count);

		if (result == 1) {
			return ResponseEntity.ok(Map.of("code", "200", "msg", "购买成功，数量：" + count));
		}

		return ResponseEntity.ok(Map.of("code", "500", "msg", "数量超出库存！"));
	}

	@GetMapping("result")
	public HttpEntity<Map<String, Object>> result() {
		return ResponseEntity.ok(Map.of("redisson", shop.readAllMap()));
	}

}
