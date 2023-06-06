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

package com.livk.browscap.mvc.example.controller;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.servlet.UserAgentContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * UserAgentController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("user-agent")
public class UserAgentController {

	@GetMapping
	public HttpEntity<Map<String, Capabilities>> get(@UserAgentInfo Capabilities capabilities) {
		Map<String, Capabilities> map = Map.of(UUID.randomUUID().toString(), capabilities,
			UUID.randomUUID().toString(), UserAgentContextHolder.getUserAgentContext().unwrap(Capabilities.class));
		return ResponseEntity.ok(map);
	}
}
