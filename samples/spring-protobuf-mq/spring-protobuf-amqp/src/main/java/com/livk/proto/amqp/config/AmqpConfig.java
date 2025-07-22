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

package com.livk.proto.amqp.config;

import com.livk.proto.amqp.converter.UserAmqpProtobufMessageConverter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author livk
 */
@Configuration
public class AmqpConfig {

	public static final String TOPIC_NAME = "amqp-protobuf";

	@Bean
	public MessageConverter protobufMessageConverter() {
		return new UserAmqpProtobufMessageConverter(new SimpleMessageConverter());
	}

	@Bean
	public Queue directQueue() {
		return new Queue(TOPIC_NAME, true, false, false);
	}

}
