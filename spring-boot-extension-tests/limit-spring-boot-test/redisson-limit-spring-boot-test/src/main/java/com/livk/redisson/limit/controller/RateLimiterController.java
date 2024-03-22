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

package com.livk.redisson.limit.controller;

import com.livk.context.limit.annotation.Limit;
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
