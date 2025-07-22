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

package com.livk.proto.kafka.converter;

import com.livk.proto.User;
import com.livk.proto.UserConverter;
import org.apache.kafka.common.serialization.Serializer;

/**
 * @author livk
 */
public class ProtobufSerializer<T> implements Serializer<T> {

	private final UserConverter converter = UserConverter.INSTANCE;

	@Override
	public byte[] serialize(String topic, T data) {
		if (data instanceof User user) {
			return converter.convert(user);
		}
		return new byte[0];
	}

}
