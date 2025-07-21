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

package com.livk.provider;

import com.livk.amqp.config.DirectConfig;
import com.livk.amqp.config.FanoutConfig;
import com.livk.amqp.config.HeadersConfig;
import com.livk.amqp.config.TopicConfig;
import com.livk.amqp.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
public class RabbitMqConsumer {

	@RabbitListener(queuesToDeclare = @Queue(DirectConfig.RABBIT_DIRECT_TOPIC))
	public <T> void consumerDirect(@Payload Message<T> message) {
		log.info("direct anno consumer msg:{}", message);
	}

	@RabbitListener(queuesToDeclare = @Queue(FanoutConfig.FANOUT_EXCHANGE_QUEUE_TOPIC_A))
	public <T> void consumerFanoutA(@Payload Message<T> message) {
		log.info("fanout A anno consumer msg:{}", message);
	}

	@RabbitListener(queuesToDeclare = @Queue(FanoutConfig.FANOUT_EXCHANGE_QUEUE_TOPIC_B))
	public <T> void consumerFanoutB(@Payload Message<T> message) {
		log.info("fanout B anno consumer msg:{}", message);
	}

	@RabbitListener(queuesToDeclare = @Queue(TopicConfig.TOPIC_EXCHANGE_QUEUE_A))
	public <T> void consumerTopicA(@Payload Message<T> message) {
		log.info("topic A anno consumer msg:{}", message);
	}

	@RabbitListener(queuesToDeclare = @Queue(TopicConfig.TOPIC_EXCHANGE_QUEUE_B))
	public <T> void consumerTopicB(@Payload Message<T> message) {
		log.info("topic B anno consumer msg:{}", message);
	}

	@RabbitListener(queuesToDeclare = @Queue(TopicConfig.TOPIC_EXCHANGE_QUEUE_C))
	public <T> void consumerTopicC(@Payload Message<T> message) {
		log.info("topic C anno consumer msg:{}", message);
	}

	@RabbitListener(queuesToDeclare = @Queue(HeadersConfig.HEADERS_EXCHANGE_QUEUE_A))
	public void consumerHeadersA(org.springframework.amqp.core.Message message) throws Exception {
		MessageProperties messageProperties = message.getMessageProperties();
		String contentType = messageProperties.getContentType();
		log.info("headers A anno consumer msg:{}", new String(message.getBody(), contentType));
	}

	@RabbitListener(queuesToDeclare = @Queue(HeadersConfig.HEADERS_EXCHANGE_QUEUE_B))
	public void consumerHeadersB(org.springframework.amqp.core.Message message) throws Exception {
		MessageProperties messageProperties = message.getMessageProperties();
		String contentType = messageProperties.getContentType();
		log.info("headers B anno consumer msg:{}", new String(message.getBody(), contentType));
	}

}
