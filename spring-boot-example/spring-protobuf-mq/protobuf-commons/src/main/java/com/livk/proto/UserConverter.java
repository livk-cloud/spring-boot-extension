package com.livk.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.livk.core.mapstruct.converter.Converter;
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
