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

import org.junit.jupiter.api.Test;
import org.redisson.client.DefaultNettyHook;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.connection.SequentialDnsAddressResolverFactory;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PlaceholdersResolver;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * @author livk
 */
@SpringJUnitConfig
@TestPropertySource(properties = { "spring.redisson.config.single-server-config.address=redis://livk.com:6379",
		"spring.redisson.config.codec=!<org.redisson.codec.JsonJacksonCodec> {}" })
class ConfigPropertiesTest {

	@Autowired
	StandardEnvironment environment;

	@Test
	void test() {
		Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
		ConfigurableConversionService conversionService = environment.getConversionService();
		PlaceholdersResolver resolver = new PropertySourcesPlaceholdersResolver(environment);
		Consumer<PropertyEditorRegistry> consumer = registry -> new RedissonPropertyEditorRegistrar()
			.registerCustomEditors(registry);
		Binder binder = new Binder(sources, resolver, conversionService, consumer);
		ConfigProperties properties = binder.bind(ConfigProperties.PREFIX, ConfigProperties.class).get();
		assertEquals("redis://livk.com:6379", properties.getConfig().useSingleServer().getAddress());
		assertInstanceOf(JsonJacksonCodec.class, properties.getConfig().getCodec());
		assertInstanceOf(DefaultNettyHook.class, properties.getConfig().getNettyHook());
		assertInstanceOf(SequentialDnsAddressResolverFactory.class,
				properties.getConfig().getAddressResolverGroupFactory());
	}

}
