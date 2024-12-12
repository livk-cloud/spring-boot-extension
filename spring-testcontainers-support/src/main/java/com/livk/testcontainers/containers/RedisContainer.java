/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.testcontainers.containers;

import com.livk.testcontainers.DockerImageNames;
import lombok.Getter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

/**
 * @author livk
 */
@Getter
public class RedisContainer extends GenericContainer<RedisContainer> {

	private String password;

	public RedisContainer(DockerImageName dockerImageName) {
		super(dockerImageName);
		addExposedPorts(6379);
		this.waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));
	}

	public static RedisContainer redis() {
		return new RedisContainer(DockerImageNames.redis());
	}

	public static RedisContainer redisStack() {
		return new RedisContainer(DockerImageNames.redisStack());
	}

	@Override
	protected void configure() {
		Optional.ofNullable(password).ifPresent(s -> withCommand("--requirepass", s));
	}

	public RedisContainer withPassword(String password) {
		this.password = password;
		return this;
	}

}
