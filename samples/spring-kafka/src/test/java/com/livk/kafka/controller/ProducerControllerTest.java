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

package com.livk.kafka.controller;

import com.livk.kafka.KafkaConstant;
import com.livk.testcontainers.DockerImageNames;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class ProducerControllerTest {

	@Container
	@ServiceConnection
	static final KafkaContainer kafka = new KafkaContainer(DockerImageNames.kafka());

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers",
				() -> String.format("%s:%s", kafka.getHost(), kafka.getFirstMappedPort()));
	}

	@Autowired
	MockMvc mockMvc;

	@Test
	void producer() throws Exception {
		mockMvc.perform(get("/kafka/send")).andDo(print()).andExpect(status().isOk());

		Awaitility.waitAtMost(Duration.ofMinutes(4)).untilAsserted(() -> assertThat(messages).hasSize(3));
	}

	final List<String> messages = new ArrayList<>();

	@KafkaListener(id = "test-id", topics = KafkaConstant.TOPIC)
	void processMessage(String message) {
		this.messages.add(message);
	}

}
