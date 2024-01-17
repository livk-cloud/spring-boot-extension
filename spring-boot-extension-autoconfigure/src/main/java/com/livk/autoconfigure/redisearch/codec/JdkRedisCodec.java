/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.autoconfigure.redisearch.codec;

import org.springframework.core.ConfigurableObjectInputStream;

import java.io.*;

/**
 * The type Jdk redis codec.
 *
 * @author livk
 */
public class JdkRedisCodec extends AbstractRedisCodec<Object> {

	private ClassLoader classLoader;

	/**
	 * Instantiates a new Jdk redis codec.
	 */
	public JdkRedisCodec() {
	}

	/**
	 * Instantiates a new Jdk redis codec.
	 * @param classLoader the class loader
	 */
	public JdkRedisCodec(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	protected byte[] serialize(Object value) throws CodecException {
		if (!(value instanceof Serializable)) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload "
					+ "but received an object of type [" + value.getClass().getName() + "]");
		}
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
			objectOutputStream.writeObject(value);
			objectOutputStream.flush();
			return out.toByteArray();
		}
		catch (IOException e) {
			throw new CodecException("Cannot serialize", e);
		}
	}

	@Override
	protected Object deserialize(byte[] bytes) throws CodecException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);
			return objectInputStream.readObject();
		}
		catch (IOException | ClassNotFoundException e) {
			throw new CodecException("Cannot deserialize", e);
		}
	}

}
