/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.proto.rocketmq;

import com.livk.proto.ProtobufSend;
import com.livk.proto.User;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class RocketMqSend implements ProtobufSend<User> {

	private final RocketMQTemplate rocketMQTemplate;

	@Override
	public void send(String key, User data) {
		rocketMQTemplate.convertAndSend(key, data);
	}

}
