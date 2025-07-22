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

package com.livk.ck.r2dbc.service.impl;

import com.livk.ck.r2dbc.entity.User;
import com.livk.ck.r2dbc.repository.UserRepository;
import com.livk.ck.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author livk
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public Flux<User> list() {
		return userRepository.findAll();
	}

	@Override
	public Mono<Void> remove(Mono<Integer> id) {
		return userRepository.deleteById(id);
	}

	@Override
	public Mono<Void> save(Mono<User> userMono) {
		return userMono.flatMap(userRepository::save);
	}

}
