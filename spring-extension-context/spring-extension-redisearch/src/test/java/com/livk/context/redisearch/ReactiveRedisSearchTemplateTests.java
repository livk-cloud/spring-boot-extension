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
import com.redis.testcontainers.RedisStackContainer;
import io.lettuce.core.RedisConnectionException;
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
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
@SpringJUnitConfig(RediSearchConfig.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class ReactiveRedisSearchTemplateTests {

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
	void executeShouldReturnPing() {
		ReactiveRedisSearchTemplate<String, Object> template = createTemplate(null);

		StepVerifier.create(template.execute(BaseRedisReactiveCommands::ping)).expectNext("PONG").verifyComplete();
	}

	@Test
	void executeManyShouldReturnValues() {
		ReactiveRedisSearchTemplate<String, Object> template = createTemplate(null);

		Flux<String> flux = template.executeMany((commands) -> commands.ping().flux().concatWith(commands.ping()));

		StepVerifier.create(flux).expectNext("PONG", "PONG").verifyComplete();
	}

	/**
	 * usingWhen 在订阅前不借连接：未订阅的 Mono 不应占满池。
	 */
	@Test
	void unusedPublisherShouldNotBorrowConnection() {
		ReactiveRedisSearchTemplate<String, Object> template = createTemplate(singleConnectionPool());

		Mono<String> unused = template.execute((commands) -> commands.ping().delayElement(Duration.ofHours(1)));

		assertThat(unused).isNotNull();
		StepVerifier.create(template.execute(BaseRedisReactiveCommands::ping)).expectNext("PONG").verifyComplete();
	}

	/**
	 * 池大小为 1：已订阅且未完成的发布者应占用连接，后续借用超时。
	 */
	@Test
	void executeShouldHoldConnectionUntilPublisherCompletes() throws InterruptedException {
		ReactiveRedisSearchTemplate<String, Object> template = createTemplate(singleConnectionPool());

		CountDownLatch subscribed = new CountDownLatch(1);
		CountDownLatch completed = new CountDownLatch(1);
		AtomicReference<String> result = new AtomicReference<>();
		AtomicReference<Throwable> error = new AtomicReference<>();

		Mono<String> delayed = template.execute((commands) -> commands.ping()
			.doOnSubscribe((subscription) -> subscribed.countDown())
			.delayElement(Duration.ofSeconds(2)));

		Disposable disposable = delayed.subscribe(result::set, (ex) -> {
			error.set(ex);
			completed.countDown();
		}, completed::countDown);
		try {
			assertThat(subscribed.await(1, TimeUnit.SECONDS)).isTrue();

			assertThatThrownBy(() -> template.execute(BaseRedisReactiveCommands::ping).block(Duration.ofMillis(500)))
				.isInstanceOf(RedisConnectionException.class);

			assertThat(completed.await(5, TimeUnit.SECONDS)).isTrue();
			assertThat(error.get()).isNull();
			assertThat(result.get()).isEqualTo("PONG");

			StepVerifier.create(template.execute(BaseRedisReactiveCommands::ping)).expectNext("PONG").verifyComplete();
		}
		finally {
			disposable.dispose();
		}
	}

	@Test
	void executeManyShouldHoldConnectionUntilPublisherCompletes() throws InterruptedException {
		ReactiveRedisSearchTemplate<String, Object> template = createTemplate(singleConnectionPool());

		CountDownLatch subscribed = new CountDownLatch(1);
		CountDownLatch completed = new CountDownLatch(1);
		AtomicReference<String> result = new AtomicReference<>();
		AtomicReference<Throwable> error = new AtomicReference<>();

		Flux<String> delayed = template.executeMany((commands) -> commands.ping()
			.doOnSubscribe((subscription) -> subscribed.countDown())
			.delayElement(Duration.ofSeconds(2))
			.flux());

		Disposable disposable = delayed.subscribe(result::set, (ex) -> {
			error.set(ex);
			completed.countDown();
		}, completed::countDown);
		try {
			assertThat(subscribed.await(1, TimeUnit.SECONDS)).isTrue();

			assertThatThrownBy(
					() -> template.executeMany((commands) -> commands.ping().flux()).blockFirst(Duration.ofMillis(500)))
				.isInstanceOf(RedisConnectionException.class);

			assertThat(completed.await(5, TimeUnit.SECONDS)).isTrue();
			assertThat(error.get()).isNull();
			assertThat(result.get()).isEqualTo("PONG");

			StepVerifier.create(template.execute(BaseRedisReactiveCommands::ping)).expectNext("PONG").verifyComplete();
		}
		finally {
			disposable.dispose();
		}
	}

	private ReactiveRedisSearchTemplate<String, Object> createTemplate(
			GenericObjectPoolConfig<StatefulRedisModulesConnection<String, Object>> poolConfig) {
		RedisCodec<String, Object> redisCodec = RedisSearchCodecs.json(new JsonMapper());
		ReactiveRedisSearchTemplate<String, Object> template = new ReactiveRedisSearchTemplate<>(this.factory,
				redisCodec);
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
