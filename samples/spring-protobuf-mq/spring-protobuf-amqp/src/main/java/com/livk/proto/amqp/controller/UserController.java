/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
