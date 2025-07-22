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

package com.livk.autoconfigure.redisearch;

import com.livk.context.redisearch.StringRediSearchTemplate;
import io.lettuce.core.resource.ClientResources;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RediSearchAutoConfigurationTests {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withBean(Jackson2ObjectMapperBuilder.class, Jackson2ObjectMapperBuilder::new)
		.withConfiguration(AutoConfigurations.of(RediSearchAutoConfiguration.class))
		.withUserConfiguration(Config.class);

	@Test
	void clientResources() {
		contextRunner.run((context) -> assertThat(context).hasSingleBean(ClientResources.class));
	}

	@Test
	void lettuceConnectionFactory() {
		contextRunner.run((context) -> assertThat(context).hasSingleBean(LettuceModConnectionFactory.class));
	}

	@Test
	void rediSearchTemplate() {
		contextRunner.run((context) -> assertThat(context).hasBean("rediSearchTemplate"));
	}

	@Test
	void stringRediSearchTemplate() {
		contextRunner.run((context) -> assertThat(context).hasSingleBean(StringRediSearchTemplate.class));
	}

	@TestConfiguration
	@EnableConfigurationProperties(RediSearchProperties.class)
	static class Config {

	}

}
