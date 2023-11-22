/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.spring;

import com.livk.filter.context.TenantContextHolder;
import com.livk.spring.factory.UUIDRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * FilterController
 * </p>
 *
 * @author livk
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class FilterController {

	private final UUIDRequest uuidRequest;

	@GetMapping("tenant")
	public HttpEntity<String> tenant(@RequestHeader(TenantContextHolder.ATTRIBUTES) String tenant) {
		log.info("tenant:{}", TenantContextHolder.getTenantId());
		log.info("uuid:{}", uuidRequest.currentUUID().toString());
		return ResponseEntity.ok(tenant);
	}

	@GetMapping("uuid")
	public HttpEntity<String> uuid() {
		return ResponseEntity.ok(uuidRequest.currentUUID().toString());
	}

}
