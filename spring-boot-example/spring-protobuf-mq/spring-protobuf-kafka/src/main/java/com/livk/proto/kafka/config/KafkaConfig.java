package com.livk.proto.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author livk
 */
@Configuration
public class KafkaConfig {

	public static final String TOPIC_NAME = "kafka-protobuf";

	@Bean
	public NewTopic myTopic() {
		return new NewTopic(TOPIC_NAME, 1, (short) 1);
	}

}
