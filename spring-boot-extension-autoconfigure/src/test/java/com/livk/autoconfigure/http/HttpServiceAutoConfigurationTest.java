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

package com.livk.autoconfigure.http;

import com.livk.commons.util.ClassUtils;
import com.livk.context.http.annotation.HttpProvider;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class HttpServiceAutoConfigurationTest {

	static {
		try (DynamicType.Unloaded<Object> unloaded = new ByteBuddy().subclass(Object.class)
			.name("com.livk.http.marker.HttpMarker")
			.make()) {
			Class<?> loaded = unloaded.load(ClassUtils.getDefaultClassLoader(), ClassLoadingStrategy.Default.INJECTION)
				.getLoaded();
			assertThat(loaded).isNotNull();
		}
	}

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(Config.class)
		.withConfiguration(AutoConfigurations.of(HttpServiceAutoConfiguration.class));

	@Test
	void test() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(RemoteService.class);
		});
	}

	@TestConfiguration
	@AutoConfigurationPackage
	static class Config {

	}

	@HttpProvider
	interface RemoteService {

	}

}
