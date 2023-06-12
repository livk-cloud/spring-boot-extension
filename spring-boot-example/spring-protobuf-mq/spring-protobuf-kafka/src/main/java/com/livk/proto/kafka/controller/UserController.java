package com.livk.proto.kafka.controller;

import com.livk.proto.User;
import com.livk.proto.kafka.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

	private final KafkaTemplate<String, User> kafkaTemplate;

	@GetMapping("send")
	public void send() {
		ThreadLocalRandom current = ThreadLocalRandom.current();
		for (int i = 0; i < 10; i++) {
			User user = new User(100L + i, "kafka", "serializer@kafka.com", current.nextInt(0, 2));
			kafkaTemplate.send(new ProducerRecord<>(KafkaConfig.TOPIC_NAME, Long.toString(user.id()), user));
		}
	}
}
