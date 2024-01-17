/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.local.lock;

import com.livk.core.lock.LockScope;
import com.livk.core.lock.annotation.OnLock;
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
 */
@RestController
@RequestMapping("shop")
public class ShopController {

	private Integer num = 500;

	private int buyCount = 0;

	private int buySucCount = 0;

	@PostMapping("/buy/local")
	@OnLock(key = "shop", scope = LockScope.STANDALONE_LOCK)
	public HttpEntity<Map<String, Object>> buyLocal(@RequestParam(defaultValue = "2") Integer count) {
		buyCount++;
		if (num >= count) {
			num -= count;
			buySucCount++;
			Map<String, Integer> msg = Map.of("购买成功数量", count, "总计购买次数", buyCount, "购买成功次数", buySucCount);
			return ResponseEntity.ok(Map.of("code", "200", "msg", msg));
		}
		else {
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
	public HttpEntity<Map<String, Object>> result() {
		Map<String, Integer> local = Map.of("num", num, "buyCount", buyCount, "buySucCount", buySucCount);
		return ResponseEntity.ok(Map.of("local", local));
	}

}
