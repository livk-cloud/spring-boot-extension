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

package com.livk.proto.pulsar.controller;

import com.livk.testcontainers.DockerImageNames;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PulsarContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class UserControllerTest {

	@Container
	@ServiceConnection
	static final PulsarContainer pulsar = new PulsarContainer(DockerImageNames.pulsar()).withStartupAttempts(2)
		.withStartupTimeout(Duration.ofMinutes(3))
		.withExposedPorts(PulsarContainer.BROKER_PORT, PulsarContainer.BROKER_HTTP_PORT);

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.pulsar.client.service-url",
				() -> "pulsar://" + pulsar.getHost() + ":" + pulsar.getFirstMappedPort());
	}

	@Autowired
	MockMvc mockMvc;

	@Test
	void send() throws Exception {
		mockMvc.perform(get("/user/send")).andExpect(status().isOk());
	}

}
