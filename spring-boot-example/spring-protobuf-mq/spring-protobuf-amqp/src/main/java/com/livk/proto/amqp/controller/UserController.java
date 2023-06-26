package com.livk.proto.amqp.controller;

import com.livk.proto.BaseUserController;
import com.livk.proto.User;
import com.livk.proto.amqp.AmqpSend;
import com.livk.proto.amqp.config.AmqpConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseUserController {

	public UserController(AmqpSend amqpSend) {
		super(amqpSend);
	}

	@Override
	protected String key() {
		return AmqpConfig.TOPIC_NAME;
	}

	@Override
	protected User create(int i) {
		return new User(100L + i, "amqp", "serializer@amqp.com", current.nextInt(0, 2));
	}
}
