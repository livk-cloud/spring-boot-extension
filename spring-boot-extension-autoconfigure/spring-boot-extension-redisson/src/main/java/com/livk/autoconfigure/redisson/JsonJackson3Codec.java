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

package com.livk.autoconfigure.redisson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author livk
 */
@Slf4j
public class JsonJackson3Codec extends BaseCodec {

	public static final JsonJackson3Codec INSTANCE = new JsonJackson3Codec();

	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE,
			getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility = JsonAutoDetect.Visibility.NONE,
			isGetterVisibility = JsonAutoDetect.Visibility.NONE)
	public static class ThrowableMixIn {

	}

	protected final JsonMapper objectMapper;

	private final Encoder encoder;

	private final Decoder<Object> decoder;

	public JsonJackson3Codec() {
		this(JsonMapper.builder());
	}

	public JsonJackson3Codec(ClassLoader classLoader) {
		this(createObjectMapper(classLoader, new ObjectMapper()));
	}

	public JsonJackson3Codec(JsonMapper.Builder builder) {
		this.objectMapper = init(builder);
		warmup();
		this.encoder = new Jackson3Encoder(objectMapper);
		this.decoder = new Jackson3Decoder(objectMapper);
	}

	private static volatile boolean warmedUp = false;

	private void warmup() {
		if (getValueEncoder() == null || getValueDecoder() == null || warmedUp) {
			return;
		}
		warmedUp = true;

		ByteBuf buf = null;
		try {
			buf = getValueEncoder().encode("test");
			getValueDecoder().decode(buf, null);
		}
		catch (IOException e) {
			log.error("Warmup failed", e);
		}
		finally {
			if (buf != null) {
				buf.release();
			}
		}
	}

	protected static JsonMapper.Builder createObjectMapper(ClassLoader cl, ObjectMapper om) {
		return JsonMapper.builder().typeFactory(om.getTypeFactory().withClassLoader(cl));
	}

	protected JsonMapper init(JsonMapper.Builder builder) {
		PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build();
		JsonInclude.Value includeValue = JsonInclude.Value.construct(JsonInclude.Include.NON_NULL,
				JsonInclude.Include.NON_NULL);
		return builder.activateDefaultTyping(ptv, DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
			.changeDefaultVisibility(v -> v.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE))
			.changeDefaultPropertyInclusion(value -> value.withOverrides(includeValue))
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
			.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
			.addMixIn(Throwable.class, ThrowableMixIn.class)
			.build();
	}

	@Override
	public Decoder<Object> getValueDecoder() {
		return decoder;
	}

	@Override
	public Encoder getValueEncoder() {
		return encoder;
	}

	@Override
	public ClassLoader getClassLoader() {
		ClassLoader cl = objectMapper.getTypeFactory().getClassLoader();
		return cl != null ? cl : super.getClassLoader();
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	private static class Jackson3Encoder implements Encoder {

		private final JsonMapper objectMapper;

		Jackson3Encoder(JsonMapper objectMapper) {
			this.objectMapper = objectMapper;
		}

		@Override
		public ByteBuf encode(Object in) throws IOException {
			ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
			try (ByteBufOutputStream os = new ByteBufOutputStream(out)) {
				objectMapper.writeValue((OutputStream) os, in);
				return os.buffer();
			}
			catch (IOException e) {
				out.release();
				throw e;
			}
			catch (Exception e) {
				out.release();
				throw new IOException(e);
			}
		}

	}

	private static class Jackson3Decoder implements Decoder<Object> {

		private final JsonMapper objectMapper;

		Jackson3Decoder(JsonMapper objectMapper) {
			this.objectMapper = objectMapper;
		}

		@Override
		public Object decode(ByteBuf buf, State state) {
			return objectMapper.readValue((InputStream) new ByteBufInputStream(buf), Object.class);
		}

	}

}
