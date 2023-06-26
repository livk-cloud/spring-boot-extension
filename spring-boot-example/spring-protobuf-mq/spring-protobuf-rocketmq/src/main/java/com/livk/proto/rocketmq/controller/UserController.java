package com.livk.proto.rocketmq.controller;

import com.livk.proto.BaseUserController;
import com.livk.proto.User;
import com.livk.proto.rocketmq.RocketMqSend;
import com.livk.proto.rocketmq.config.RocketMqConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseUserController {

	public UserController(RocketMqSend rocketMqSend) {
		super(rocketMqSend);
	}

	@Override
	protected String key() {
		return RocketMqConfig.TOPIC_NAME;
	}

	@Override
	protected User create(int i) {
		return new User(100L + i, "rocketmq", "serializer@rocketmq.com", current.nextInt(0, 2));
	}
}
