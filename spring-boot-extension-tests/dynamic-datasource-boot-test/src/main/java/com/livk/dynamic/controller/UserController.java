/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.dynamic.controller;

import com.livk.context.dynamic.annotation.DynamicSource;
import com.livk.dynamic.entity.User;
import com.livk.dynamic.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 */
@RestController
@DynamicSource("pgsql")
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

	private final UserMapper userMapper;

	@DynamicSource("mysql")
	@PostMapping("mysql")
	public HttpEntity<Boolean> mysqlSave() {
		User user = new User();
		user.setUsername("root");
		user.setPassword("123456");
		return ResponseEntity.ok(userMapper.insert(user, "user") != 0);
	}

	@DynamicSource("mysql")
	@GetMapping("mysql")
	public HttpEntity<List<User>> mysqlUser() {
		return ResponseEntity.ok(userMapper.selectList("user"));
	}

	@PostMapping("pgsql")
	public HttpEntity<Boolean> pgsqlSave() {
		User user = new User();
		user.setUsername("postgres");
		user.setPassword("123456");
		return ResponseEntity.ok(userMapper.insert(user, "\"user\"") != 0);
	}

	@GetMapping("pgsql")
	public HttpEntity<List<User>> pgsqlUser() {
		return ResponseEntity.ok(userMapper.selectList("\"user\""));
	}

}
