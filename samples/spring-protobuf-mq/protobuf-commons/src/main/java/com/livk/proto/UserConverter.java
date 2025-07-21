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

package com.livk.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.livk.context.mapstruct.converter.Converter;
import com.livk.proto.gen.UserProto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author livk
 */
@Mapper
public interface UserConverter extends Converter<User, UserProto.User> {

	UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

	default User convert(byte[] bytes) {
		try {
			UserProto.User user = UserProto.User.parseFrom(bytes);
			return getSource(user);
		}
		catch (InvalidProtocolBufferException e) {
			throw new RuntimeException(e);
		}
	}

	default byte[] convert(User user) {
		return getTarget(user).toByteArray();
	}

}
