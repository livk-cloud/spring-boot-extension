package com.livk.proto.kafka.converter;

import com.livk.proto.User;
import com.livk.proto.UserConverter;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @author livk
 */
public class UserKafkaProtobufConverter implements Deserializer<User>, Serializer<User> {

	private final UserConverter converter = UserConverter.INSTANCE;

	@Override
	public User deserialize(String topic, byte[] data) {
		return converter.convert(data);
	}

	@Override
	public byte[] serialize(String topic, User data) {
		return converter.convert(data);
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@Override
	public void close() {

	}
}
