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

package com.livk.autoconfigure.redisson;

import com.livk.testcontainers.containers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PlaceholdersResolver;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
@SpringJUnitConfig
@Testcontainers(disabledWithoutDocker = true, parallel = true)
@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
class RedissonClientFactoryTest {

	@Container
	@ServiceConnection
	static RedisContainer redis = new RedisContainer();

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redisson.config.single-server-config.address",
				() -> "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
		registry.add("spring.redisson.config.codec", () -> "!<org.redisson.codec.JsonJacksonCodec> {}");
	}

	@Autowired
	StandardEnvironment environment;

	@Autowired
	ObjectProvider<ConfigCustomizer> configCustomizers;

	@Test
	void create() {
		Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
		ConfigurableConversionService conversionService = environment.getConversionService();
		PlaceholdersResolver resolver = new PropertySourcesPlaceholdersResolver(environment);
		Consumer<PropertyEditorRegistry> consumer = registry -> new RedissonPropertyEditorRegistrar()
			.registerCustomEditors(registry);
		Binder binder = new Binder(sources, resolver, conversionService, consumer);
		ConfigProperties properties = binder.bind(ConfigProperties.PREFIX, ConfigProperties.class).get();

		RedissonClient redissonClient = RedissonClientFactory.create(properties, new RedisProperties(),
				configCustomizers);

		assertNotNull(redissonClient);
		redissonClient.shutdown();
	}

}
