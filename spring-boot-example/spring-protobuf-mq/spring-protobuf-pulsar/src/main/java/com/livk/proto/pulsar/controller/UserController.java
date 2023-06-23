package com.livk.proto.pulsar.controller;

import com.livk.proto.User;
import com.livk.proto.pulsar.config.PulsarConfig;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

	private final PulsarTemplate<User> pulsarTemplate;

	@GetMapping("send")
	public void send() throws PulsarClientException {
		ThreadLocalRandom current = ThreadLocalRandom.current();
		for (int i = 0; i < 10; i++) {
			User user = new User(100L + i, "pulsar", "serializer@pulsar.com", current.nextInt(0, 2));
			pulsarTemplate.newMessage(user)
				.withTopic(PulsarConfig.TOPIC_NAME)
				.withMessageCustomizer(builder -> builder.key(UUID.randomUUID().toString().substring(0, 5)))
				.sendAsync()
				.handle((messageId, throwable) -> throwable == null)
				.join();
		}
	}
}
