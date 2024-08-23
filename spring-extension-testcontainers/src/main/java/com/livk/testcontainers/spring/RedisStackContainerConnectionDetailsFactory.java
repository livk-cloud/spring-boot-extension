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

package com.livk.testcontainers.spring;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.testcontainers.DockerImageNames;
import com.livk.testcontainers.containers.RedisContainer;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

/**
 * <a href="https://github.com/spring-projects/spring-boot/pull/41327">redis-stack支持</a>
 *
 * @author livk
 */
@SpringFactories(ConnectionDetailsFactory.class)
class RedisStackContainerConnectionDetailsFactory
		extends ContainerConnectionDetailsFactory<RedisContainer, RedisConnectionDetails> {

	RedisStackContainerConnectionDetailsFactory() {
		super(DockerImageNames.REDIS_STACK_IMAGE);
	}

	@Override
	protected RedisConnectionDetails getContainerConnectionDetails(ContainerConnectionSource<RedisContainer> source) {
		return new RedisStackContainerConnectionDetails(source);
	}

	private static final class RedisStackContainerConnectionDetails extends ContainerConnectionDetails<RedisContainer>
			implements RedisConnectionDetails {

		private RedisStackContainerConnectionDetails(ContainerConnectionSource<RedisContainer> source) {
			super(source);
		}

		@Override
		public Standalone getStandalone() {
			return Standalone.of(getContainer().getHost(), getContainer().getFirstMappedPort());
		}

	}

}
