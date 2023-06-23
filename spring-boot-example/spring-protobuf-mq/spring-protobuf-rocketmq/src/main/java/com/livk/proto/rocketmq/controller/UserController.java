package com.livk.proto.rocketmq.controller;

import com.livk.proto.User;
import com.livk.proto.rocketmq.config.RocketMqConfig;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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

	private final RocketMQTemplate rocketMQTemplate;

	@GetMapping("send")
	public void send() {
		ThreadLocalRandom current = ThreadLocalRandom.current();
		for (int i = 0; i < 10; i++) {
			User user = new User(100L + i, "rocketmq", "serializer@rocketmq.com", current.nextInt(0, 2));
			rocketMQTemplate.convertAndSend(RocketMqConfig.TOPIC_NAME, user);
		}
	}
}
