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

package com.livk.mqtt.handler;

import com.livk.mqtt.config.MqttSenderConfig;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @author livk
 */
@MessagingGateway(defaultRequestChannel = MqttSenderConfig.CHANNEL_NAME_OUT)
public interface MqttSender {

	/**
	 * 发送信息到MQTT服务器
	 * @param data 发送的文本
	 */
	void sendToMqtt(String data);

	/**
	 * 发送信息到MQTT服务器
	 * @param topic 主题
	 * @param payload 消息主体
	 */
	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);

	/**
	 * 发送信息到MQTT服务器
	 * @param topic 主题
	 * @param qos 对消息处理的几种机制。 0 表示的是订阅者没收到消息不会再次发送，消息会丢失。 1
	 * 表示的是会尝试重试，一直到接收到消息，但这种情况可能导致订阅者收到多次重复消息。 2 多了一次去重的动作，确保订阅者收到的消息有一次。
	 * @param payload 消息主体
	 */
	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);

}
