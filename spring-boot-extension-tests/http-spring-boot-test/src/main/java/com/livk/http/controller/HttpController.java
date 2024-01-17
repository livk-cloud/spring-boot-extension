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

package com.livk.http.controller;

import com.google.common.collect.Maps;
import com.livk.http.service.JavaService;
import com.livk.http.service.SpringBootService;
import com.livk.http.service.SpringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * HttpController
 * </p>
 *
 * @author livk
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class HttpController {

	private final SpringBootService bootService;

	private final SpringService springService;

	private final JavaService javaService;

	@GetMapping("get")
	public HttpEntity<Map<String, String>> get() {
		Map<String, String> result = Maps.newHashMap();
		result.putAll(bootService.springBoot());
		result.putAll(springService.spring());
		result.putAll(javaService.java());
		return ResponseEntity.ok(result);
	}

	@GetMapping("/rpc/spring-boot")
	public Map<String, String> springBoot() {
		return Map.of("spring-boot-version", SpringBootVersion.getVersion());
	}

	@GetMapping("/rpc/spring")
	public Map<String, String> spring() {
		return Map.of("spring-version", Objects.requireNonNull(SpringVersion.getVersion()));
	}

	@GetMapping("/rpc/java")
	public Map<String, String> java() {
		return Map.of("java-version", System.getProperty("java.version"));
	}

}
