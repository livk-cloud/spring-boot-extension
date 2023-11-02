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
