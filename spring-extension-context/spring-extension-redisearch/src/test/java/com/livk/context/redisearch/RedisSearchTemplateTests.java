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

package com.livk.context.redisearch;

import com.livk.context.redisearch.codec.RedisSearchCodecs;
import com.livk.testcontainers.DockerImageNames;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.testcontainers.RedisStackContainer;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.BaseRedisAsyncCommands;
import io.lettuce.core.api.reactive.BaseRedisReactiveCommands;
import io.lettuce.core.codec.RedisCodec;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
@SpringJUnitConfig(RediSearchConfig.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedisSearchTemplateTests {

	@Container
	@ServiceConnection
	static final RedisStackContainer redisStack = new RedisStackContainer(DockerImageNames.redisStack());

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("redisearch.host", redisStack::getHost);
		registry.add("redisearch.port", redisStack::getFirstMappedPort);
	}

	@Autowired
	RedisSearchConnectionFactory factory;

	@Test
	void test() throws Exception {
		RedisSearchTemplate<String, Object> template = createTemplate(null);

		assertThat(template.executeAsync(BaseRedisAsyncCommands::ping).get()).isEqualTo("PONG");
		assertThat(template.executeReactive(BaseRedisReactiveCommands::ping).block()).isEqualTo("PONG");
	}

	/**
	 * 池大小为 1：若 reactive 发布者未完成前连接仍被占用，同步借出会超时。
	 * <p>
	 * 当前实现在 {@code executeReactive} 返回时就把连接还池，同步借出成功，本用例会失败。
	 */
	@Test
	void executeReactiveShouldHoldConnectionUntilPublisherCompletes() {
		RedisSearchTemplate<String, Object> template = createTemplate(singleConnectionPool());

		Mono<String> delayed = template
			.executeReactive((commands) -> commands.ping().delayElement(Duration.ofSeconds(2)));

		assertThatThrownBy(() -> template.executeSync(RedisModulesCommands::ping))
			.isInstanceOf(RedisConnectionException.class);

		assertThat(delayed.block(Duration.ofSeconds(5))).isEqualTo("PONG");
		assertThat(template.<String>executeSync(RedisModulesCommands::ping)).isEqualTo("PONG");
	}

	/**
	 * 池大小为 1：async Future 未完成前连接应仍被占用。
	 * <p>
	 * 用 {@code blpop} 保证 Future 在超时前不会完成。当前实现返回 Future 时已还池，本用例会失败。
	 */
	@Test
	void executeAsyncShouldHoldConnectionUntilFutureCompletes() {
		RedisSearchTemplate<String, Object> template = createTemplate(singleConnectionPool());

		RedisFuture<?> future = template
			.executeAsync((commands) -> commands.blpop(5, "redisearch-template-async-hold"));
		try {
			assertThatThrownBy(() -> template.executeSync(RedisModulesCommands::ping))
				.isInstanceOf(RedisConnectionException.class);
		}
		finally {
			future.cancel(true);
		}
	}

	private RedisSearchTemplate<String, Object> createTemplate(
			GenericObjectPoolConfig<StatefulRedisModulesConnection<String, Object>> poolConfig) {
		RedisCodec<String, Object> redisCodec = RedisSearchCodecs.json(new JsonMapper());
		RedisSearchTemplate<String, Object> template = new RedisSearchTemplate<>(this.factory, redisCodec);
		if (poolConfig != null) {
			template.setPoolConfig(poolConfig);
		}
		template.afterPropertiesSet();
		return template;
	}

	private static GenericObjectPoolConfig<StatefulRedisModulesConnection<String, Object>> singleConnectionPool() {
		GenericObjectPoolConfig<StatefulRedisModulesConnection<String, Object>> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(1);
		config.setMaxWait(Duration.ofMillis(300));
		return config;
	}

}
