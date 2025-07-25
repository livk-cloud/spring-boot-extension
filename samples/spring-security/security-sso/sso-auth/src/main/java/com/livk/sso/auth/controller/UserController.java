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

package com.livk.sso.auth.controller;

import com.livk.sso.commons.util.SecurityContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

	@GetMapping("list")
	public HttpEntity<String> list() {
		log.info("{}", SecurityContextUtils.getUser());
		return ResponseEntity.ok("list");
	}

	@PutMapping("update")
	public HttpEntity<String> update() {
		log.info("{}", SecurityContextUtils.getUser());
		return ResponseEntity.ok("update");
	}

}
