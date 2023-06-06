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

package com.livk.ck.jdbc.controller;

import com.livk.ck.jdbc.entity.User;
import com.livk.ck.jdbc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public HttpEntity<List<User>> list() {
		return ResponseEntity.ok(userService.list());
	}

	@DeleteMapping("/{regTime}")
	public HttpEntity<Boolean> remove(@PathVariable String regTime) {
		return ResponseEntity.ok(userService.remove(regTime));
	}

	@PostMapping
	public HttpEntity<Boolean> save(@RequestBody User user) {
		return ResponseEntity.ok(userService.save(user));
	}
}
