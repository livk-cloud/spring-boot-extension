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

package com.livk.context.curator;

import com.livk.testcontainers.containers.ZookeeperContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CuratorTemplate}-specific methods not covered by
 * {@link CuratorOperationsTests}.
 *
 * @author livk
 */
@SpringJUnitConfig(CuratorConfig.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class CuratorTemplateTests {

	@Container
	@ServiceConnection
	static final ZookeeperContainer zookeeper = new ZookeeperContainer();

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("curator.connectString",
				() -> String.format("%s:%s", zookeeper.getHost(), zookeeper.getFirstMappedPort()));
	}

	@Autowired
	CuratorTemplate template;

	@Test
	void setDataAsyncWithoutListener() {
		template.createNode("/asyncNoListener", "data".getBytes(StandardCharsets.UTF_8));
		template.setDataAsync("/asyncNoListener", "updated".getBytes(StandardCharsets.UTF_8));
		assertThat(template.getNode("/asyncNoListener")).isEqualTo("updated".getBytes(StandardCharsets.UTF_8));
		template.deleteNode("/asyncNoListener");
	}

	@Test
	void closeDoesNotThrowOnStartedFramework() {
		assertThat(template.getNode("/")).isNotNull();
	}

}
