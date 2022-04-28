package com.livk.consumer.direct;

import com.livk.amqp.config.DirectConfig;
import com.livk.amqp.config.FanoutConfig;
import com.livk.amqp.config.HeadersConfig;
import com.livk.amqp.config.TopicConfig;
import com.livk.amqp.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * <p>
 * AnnoConsumer
 * </p>
 *
 * @author livk
 * @date 2022/4/14
 */
@Slf4j
@Component
public class AnnoConsumer {

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
		var messageProperties = message.getMessageProperties();
		var contentType = messageProperties.getContentType();
		log.info("headers A anno consumer msg:{}", new String(message.getBody(), contentType));
	}

	@RabbitListener(queuesToDeclare = @Queue(HeadersConfig.HEADERS_EXCHANGE_QUEUE_B))
	public void consumerHeadersB(org.springframework.amqp.core.Message message) throws Exception {
		var messageProperties = message.getMessageProperties();
		var contentType = messageProperties.getContentType();
		log.info("headers B anno consumer msg:{}", new String(message.getBody(), contentType));
	}

}
