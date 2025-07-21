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

package com.livk.mybatisplugins.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.livk.commons.util.BeanLambda;
import com.livk.mybatisplugins.entity.PageInfo;
import com.livk.mybatisplugins.entity.User;
import com.livk.mybatisplugins.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("{id}")
	public HttpEntity<User> getById(@PathVariable Integer id) {
		return ResponseEntity.ok(userService.getById(id));
	}

	@PutMapping("{id}")
	public HttpEntity<Boolean> updateById(@PathVariable Integer id, @RequestBody User user) {
		user.setId(id);
		return ResponseEntity.ok(userService.updateById(user));
	}

	@PostMapping
	public HttpEntity<Boolean> save(@RequestBody User user) {
		return ResponseEntity.ok(userService.save(user));
	}

	@DeleteMapping("{id}")
	public HttpEntity<Boolean> deleteById(@PathVariable Integer id) {
		return ResponseEntity.ok(userService.deleteById(id));
	}

	@GetMapping
	public HttpEntity<PageInfo<User>> page(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		try (Page<User> page = PageHelper.<User>startPage(pageNum, pageSize)
			.countColumn(BeanLambda.fieldName(User::getId))
			.doSelectPage(userService::list)) {
			PageInfo<User> result = new PageInfo<>(page);
			return ResponseEntity.ok(result);
		}
	}

}
