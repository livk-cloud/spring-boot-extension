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

package com.livk.autoconfigure.disruptor;

import com.livk.context.disruptor.annotation.DisruptorEvent;
import com.livk.context.disruptor.support.SpringDisruptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class DisruptorAutoConfigurationTest {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(Config.class)
		.withConfiguration(AutoConfigurations.of(DisruptorAutoConfiguration.class));

	@Test
	void test() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(SpringDisruptor.class);
			assertThat(context).hasBean("disruptorEntity");
		});
	}

	@TestConfiguration
	@AutoConfigurationPackage
	static class Config {

	}

	@DisruptorEvent("disruptorEntity")
	static class DisruptorEntity {

	}

}
