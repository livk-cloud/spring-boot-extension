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

package com.livk.mapstruct.controller;

import com.livk.context.mapstruct.converter.MapstructService;
import com.livk.mapstruct.entity.User;
import com.livk.mapstruct.entity.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

	public static final List<User> USERS = List.of(
			new User().setId(1).setUsername("livk1").setPassword("123456").setType(1).setCreateTime(new Date()),
			new User().setId(2).setUsername("livk2").setPassword("123456").setType(2).setCreateTime(new Date()),
			new User().setId(3).setUsername("livk3").setPassword("123456").setType(3).setCreateTime(new Date()));

	// 自定义双向转换
	private final MapstructService service;

	// spring单向转换
	private final ConversionService conversionService;

	@GetMapping
	public HttpEntity<Map<String, List<UserVO>>> list() {
		List<UserVO> userVOS = USERS.stream()
			.map(user -> conversionService.convert(user, UserVO.class))
			.filter(Objects::nonNull)
			.toList();
		return ResponseEntity.ok(Map.of("spring", userVOS, "customize", service.convert(USERS, UserVO.class).toList()));
	}

	@GetMapping("/{id}")
	public HttpEntity<Map<String, UserVO>> getById(@PathVariable Integer id) {
		User u = USERS.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(new User());
		UserVO userVOSpring = conversionService.convert(u, UserVO.class);
		return ResponseEntity.ok(Map.of("customize", service.convert(u, UserVO.class), "spring", userVOSpring));
	}

}
