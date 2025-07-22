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

package com.livk.mqtt;

import com.livk.mqtt.handler.MqttSender;
import com.livk.testcontainers.DockerImageNames;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

/**
 * @author livk
 */
@SpringBootTest(classes = MqttApp.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class MqttTest {

	@Container
	@ServiceConnection
	@SuppressWarnings("deprecation")
	static final RabbitMQContainer rabbit = new RabbitMQContainer(DockerImageNames.rabbitmq()).withExposedPorts(1883)
		.withPluginsEnabled("rabbitmq_mqtt", "rabbitmq_web_mqtt")
		.withAdminPassword("admin")
		.withStartupTimeout(Duration.ofMinutes(4));

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.mqtt.url", () -> "tcp://" + rabbit.getHost() + ":" + rabbit.getFirstMappedPort());
		registry.add("spring.mqtt.username", rabbit::getAdminUsername);
		registry.add("spring.mqtt.password", rabbit::getAdminPassword);
	}

	@Autowired
	MqttSender mqttSender;

	@Test
	void test() {
		for (int i = 0; i < 100; i++) {
			mqttSender.sendToMqtt("hello" + (i + 1));
		}
	}

}
