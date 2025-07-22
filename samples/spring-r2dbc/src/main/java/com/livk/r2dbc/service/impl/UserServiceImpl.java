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

package com.livk.r2dbc.service.impl;

import com.livk.r2dbc.domain.User;
import com.livk.r2dbc.repository.UserRepository;
import com.livk.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

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
	public Mono<User> getById(Mono<Long> id) {
		return userRepository.findById(id);
	}

	@Override
	public Mono<Void> save(Mono<User> userMono) {
		return userMono.flatMap(user -> userRepository.save(user)
			.flatMap(u1 -> Objects.nonNull(u1.id()) ? Mono.empty()
					: Mono.defer(() -> Mono.error(new RuntimeException()))));
	}

	@Override
	public Mono<Void> updateById(Mono<Long> id, Mono<User> userMono) {
		return userMono
			.flatMap(monoUser -> userRepository.findById(id)
				.switchIfEmpty(Mono.error(new RuntimeException("Id Not Found!")))
				.flatMap(user -> userRepository.save(new User(user.id(), monoUser.username(), monoUser.password()))))
			.flatMap(mono -> Mono.empty());
	}

	@Override
	public Mono<Void> deleteById(Mono<Long> id) {
		return userRepository.deleteById(id);
	}

}
