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

package com.livk.autoconfigure.oss;

import com.livk.autoconfigure.oss.support.OSSTemplate;
import com.livk.autoconfigure.oss.support.minio.MinioService;
import com.livk.commons.util.ClassUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class OSSAutoConfigurationTest {

	static {
		try (DynamicType.Unloaded<Object> unloaded = new ByteBuddy().subclass(Object.class)
			.name("com.livk.oss.marker.OSSMarker")
			.make()) {
			Class<?> loaded = unloaded.load(ClassUtils.getDefaultClassLoader(), ClassLoadingStrategy.Default.INJECTION)
				.getLoaded();
			assertThat(loaded).isNotNull();
		}
	}

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(Config.class)
		.withConfiguration(AutoConfigurations.of(OSSAutoConfiguration.class))
		.withPropertyValues("spring.oss.url=minio:http://livk.com:9000", "spring.oss.access-key=admin",
				"spring.oss.secret-key=1375632510");

	@Test
	void test() {
		this.contextRunner.run(context -> {
			assertThat(context).hasSingleBean(OSSTemplate.class);
			assertThat(context).hasSingleBean(MinioService.class);
		});
	}

	@TestConfiguration
	@EnableConfigurationProperties(OSSProperties.class)
	static class Config {

	}

}
