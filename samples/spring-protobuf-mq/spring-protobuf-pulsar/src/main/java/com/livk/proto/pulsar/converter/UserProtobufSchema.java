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

package com.livk.proto.pulsar.converter;

import com.livk.proto.User;
import com.livk.proto.UserConverter;
import org.apache.pulsar.client.impl.schema.AbstractSchema;
import org.apache.pulsar.common.schema.SchemaInfo;
import org.apache.pulsar.common.schema.SchemaType;
import org.apache.pulsar.shade.io.netty.buffer.ByteBuf;
import org.apache.pulsar.shade.io.netty.buffer.ByteBufUtil;

/**
 * @author livk
 */
public class UserProtobufSchema extends AbstractSchema<User> {

	private final UserConverter userConverter = UserConverter.INSTANCE;

	@Override
	public User decode(ByteBuf byteBuf) {
		byte[] bytes = ByteBufUtil.getBytes(byteBuf);
		return decode(bytes);
	}

	@Override
	public User decode(byte[] bytes) {
		return userConverter.convert(bytes);
	}

	@Override
	public byte[] encode(User message) {
		return userConverter.convert(message);
	}

	@Override
	public SchemaInfo getSchemaInfo() {
		return SchemaInfo.builder().name("protobuf").type(SchemaType.BYTES).schema(new byte[0]).build();
	}

}
