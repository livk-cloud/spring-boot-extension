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

package com.livk.caffeine.controller;

import com.livk.caffeine.annotation.DoubleCache;
import com.livk.caffeine.enums.CacheType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author livk
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
