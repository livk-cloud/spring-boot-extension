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

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Objects;

/**
 * @author livk
 */
@Slf4j
@Configuration
public class MqttReceiverConfig {

	public static final String CHANNEL_NAME_IN = "mqttInboundChannel";

	/**
	 * MQTT信息通道（消费者）
	 */
	@Bean(name = CHANNEL_NAME_IN)
	public MessageChannel mqttInboundChannel() {
		return new DirectChannel();
	}

	/**
	 * MQTT消息订阅绑定（消费者）
	 */
	@Bean
	public MessageProducer inbound(MqttPahoClientFactory factory, MqttProperties mqttProperties) {
		// 可以同时消费（订阅）多个Topic
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				mqttProperties.getReceiver().getClientId(), factory, mqttProperties.getReceiver().getDefaultTopic());
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		// 设置订阅通道
		adapter.setOutputChannel(mqttInboundChannel());
		return adapter;
	}

	/**
	 * MQTT消息处理器（消费者）
	 */
	@Bean
	@ServiceActivator(inputChannel = CHANNEL_NAME_IN)
	public MessageHandler handler() {
		return message -> {
			String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
			String msg = message.getPayload().toString();
			log.info("接收到订阅消息:\ttopic:{}\tmessage:{}", topic, msg);
		};
	}

}
