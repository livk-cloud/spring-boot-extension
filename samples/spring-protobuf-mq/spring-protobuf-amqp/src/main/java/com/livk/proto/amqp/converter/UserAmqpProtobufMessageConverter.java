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

package com.livk.proto.amqp.converter;

import com.livk.proto.User;
import com.livk.proto.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.jspecify.annotations.NonNull;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class UserAmqpProtobufMessageConverter extends AbstractMessageConverter {

	private final MessageConverter defaultMessageConverter;

	private final UserConverter userConverter = UserConverter.INSTANCE;

	@NonNull
	@Override
	protected Message createMessage(@NonNull Object object, @NonNull MessageProperties messageProperties) {
		if (object instanceof User user) {
			byte[] byteArray = userConverter.convert(user);
			return new Message(byteArray, messageProperties);
		}
		return defaultMessageConverter.toMessage(object, messageProperties);
	}

	@NonNull
	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		return userConverter.convert(message.getBody());
	}

}
