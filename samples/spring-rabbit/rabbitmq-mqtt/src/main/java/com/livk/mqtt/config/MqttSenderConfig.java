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

package com.livk.mqtt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author livk
 */
@Configuration
public class MqttSenderConfig {

	public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

	/**
	 * MQTT信息通道（生产者）
	 */
	@Bean(name = CHANNEL_NAME_OUT)
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	/**
	 * MQTT消息处理器（生产者）
	 */
	@Bean
	@ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
	public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory, MqttProperties mqttProperties) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttProperties.getSender().getClientId(),
				mqttPahoClientFactory);
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(mqttProperties.getSender().getDefaultTopic());
		return messageHandler;
	}

}
