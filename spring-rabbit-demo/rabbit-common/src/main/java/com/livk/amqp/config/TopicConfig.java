package com.livk.amqp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * TopicConfig
 * </p>
 *
 * @author livk
 * @date 2022/4/14
 */
@Configuration
public class TopicConfig {

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机名称
	 */
	public static final String TOPIC_EXCHANGE_NAME = "topic.exchange.livk";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列A的名称
	 */
	public static final String TOPIC_EXCHANGE_QUEUE_A = "topic.queue.a";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列B的名称
	 */
	public static final String TOPIC_EXCHANGE_QUEUE_B = "topic.queue.b";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列C的名称
	 */
	public static final String TOPIC_EXCHANGE_QUEUE_C = "topic.queue.c";

	@Bean
	public TopicExchange rabbitmqDemoTopicExchange() {
		// 配置TopicExchange交换机
		return new TopicExchange(TOPIC_EXCHANGE_NAME, true, false);
	}

	@Bean
	public Queue topicExchangeQueueA() {
		// 创建队列1
		return new Queue(TOPIC_EXCHANGE_QUEUE_A, true, false, false);
	}

	@Bean
	public Queue topicExchangeQueueB() {
		// 创建队列2
		return new Queue(TOPIC_EXCHANGE_QUEUE_B, true, false, false);
	}

	@Bean
	public Queue topicExchangeQueueC() {
		// 创建队列3
		return new Queue(TOPIC_EXCHANGE_QUEUE_C, true, false, false);
	}

	@Bean
	public Binding bindTopicA() {
		return BindingBuilder.bind(topicExchangeQueueA()).to(rabbitmqDemoTopicExchange()).with("a.*");
	}

	@Bean
	public Binding bindTopicB() {
		return BindingBuilder.bind(topicExchangeQueueB()).to(rabbitmqDemoTopicExchange()).with("a.*");
	}

	@Bean
	public Binding bindTopicC() {
		return BindingBuilder.bind(topicExchangeQueueC()).to(rabbitmqDemoTopicExchange()).with("rabbit.#");
	}

}
