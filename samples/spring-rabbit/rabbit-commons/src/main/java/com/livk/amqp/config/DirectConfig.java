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

package com.livk.amqp.config;

import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author livk
 */
@Configuration
@SpringAutoService
public class DirectConfig {

	public static final String RABBIT_DIRECT_TOPIC = "rabbitDirectTopic";

	public static final String RABBIT_DIRECT_EXCHANGE = "rabbitDirectExchange";

	public static final String RABBIT_DIRECT_BINDING = "rabbitDirectBinding";

	@Bean
	public Queue directQueue() {
		return new Queue(RABBIT_DIRECT_TOPIC, true, false, false);
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(RABBIT_DIRECT_EXCHANGE, true, false);
	}

	@Bean
	public Binding directBinding() {
		return BindingBuilder.bind(directQueue()).to(directExchange()).with(RABBIT_DIRECT_BINDING);
	}

}
