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

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * @author livk
 */
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfig {

	@Bean
	public MqttConnectOptions mqttConnectOptions(MqttProperties properties) {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName(properties.getUsername());
		options.setPassword(properties.getPassword().toCharArray());
		options.setServerURIs(properties.getUrl().split(";"));
		options.setConnectionTimeout(properties.getConnectTimeout());
		options.setKeepAliveInterval(properties.getKeepAliveInterval());
		return options;
	}

	@Bean
	public MqttPahoClientFactory mqttPahoClientFactory(MqttConnectOptions mqttConnectOptions) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(mqttConnectOptions);
		return factory;
	}

}
