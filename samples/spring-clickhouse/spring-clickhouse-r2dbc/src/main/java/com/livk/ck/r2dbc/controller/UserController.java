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

package com.livk.ck.r2dbc.controller;

import com.livk.ck.r2dbc.entity.User;
import com.livk.ck.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public HttpEntity<Flux<User>> users() {
		return ResponseEntity.ok(userService.list());
	}

	@PostMapping
	public HttpEntity<Mono<Void>> save(@RequestBody Mono<User> userMono) {
		return ResponseEntity.ok(userService.save(userMono));
	}

	@DeleteMapping("/{id}")
	public HttpEntity<Mono<Void>> delete(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(userService.remove(Mono.just(id)));
	}

}
