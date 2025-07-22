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
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author livk
 */
@Configuration
@SpringAutoService
public class FanoutConfig {

	/**
	 * RabbitMQ的FANOUT_EXCHANG交换机类型的队列 A 的名称
	 */
	public static final String FANOUT_EXCHANGE_QUEUE_TOPIC_A = "fanout.A";

	/**
	 * RabbitMQ的FANOUT_EXCHANG交换机类型的队列 B 的名称
	 */
	public static final String FANOUT_EXCHANGE_QUEUE_TOPIC_B = "fanout.B";

	/**
	 * RabbitMQ的FANOUT_EXCHANG交换机类型的名称
	 */
	public static final String FANOUT_EXCHANGE_DEMO_NAME = "fanout.exchange.demo.name";

	@Bean
	public Queue fanoutExchangeQueueA() {
		return new Queue(FANOUT_EXCHANGE_QUEUE_TOPIC_A, true, false, false);
	}

	@Bean
	public Queue fanoutExchangeQueueB() {
		return new Queue(FANOUT_EXCHANGE_QUEUE_TOPIC_B, true, false, false);
	}

	@Bean
	public FanoutExchange rabbitmqDemoFanoutExchange() {
		return new FanoutExchange(FANOUT_EXCHANGE_DEMO_NAME, true, false);
	}

	@Bean
	public Binding bindFanoutA() {
		return BindingBuilder.bind(fanoutExchangeQueueA()).to(rabbitmqDemoFanoutExchange());
	}

	@Bean
	public Binding bindFanoutB() {
		return BindingBuilder.bind(fanoutExchangeQueueB()).to(rabbitmqDemoFanoutExchange());
	}

}
