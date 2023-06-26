package com.livk.proto.pulsar.controller;

import com.livk.proto.BaseUserController;
import com.livk.proto.User;
import com.livk.proto.pulsar.PulsarSend;
import com.livk.proto.pulsar.config.PulsarConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseUserController {
	public UserController(PulsarSend pulsarSend) {
		super(pulsarSend);
	}

	@Override
	protected String key() {
		return PulsarConfig.TOPIC_NAME;
	}

	@Override
	protected User create(int i) {
		return new User(100L + i, "pulsar", "serializer@pulsar.com", current.nextInt(0, 2));
	}
}
