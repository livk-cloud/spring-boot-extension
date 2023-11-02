package com.livk.proto.kafka.controller;

import com.livk.proto.BaseUserController;
import com.livk.proto.User;
import com.livk.proto.kafka.KafkaSend;
import com.livk.proto.kafka.config.KafkaConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseUserController {

	public UserController(KafkaSend kafkaSend) {
		super(kafkaSend);
	}

	@Override
	protected String key() {
		return KafkaConfig.TOPIC_NAME;
	}

	@Override
	protected User create(int i) {
		return new User(100L + i, "kafka", "serializer@kafka.com", current.nextInt(0, 2));
	}
}
