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

package com.livk.commons.jackson.support;

import com.livk.commons.util.GenericWrapper;
import lombok.SneakyThrows;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Jackson操作默认实现
 *
 * @author livk
 */
public final class JacksonSupport extends AbstractJacksonOps implements JacksonOps, GenericWrapper<ObjectMapper> {

	private final ObjectMapper mapper;

	/**
	 * 构造JacksonSupport
	 * @param mapper jacksonMapper
	 */
	public JacksonSupport(ObjectMapper mapper) {
		super(mapper.getTypeFactory());
		this.mapper = mapper;
	}

	@SneakyThrows
	@Override
	public <T> T readValue(Object readVal, JavaType type) {
		if (readVal instanceof JsonParser jsonParser) {
			return mapper.readValue(jsonParser, type);
		}
		if (readVal instanceof JsonNode jsonNode) {
			return mapper.treeToValue(jsonNode, type);
		}
		else if (readVal instanceof File file) {
			return mapper.readValue(file, type);
		}
		else if (readVal instanceof URL url) {
			return this.readValue(Paths.get(url.toURI()), type);
		}
		else if (readVal instanceof Path path) {
			return mapper.readValue(path, type);
		}
		else if (readVal instanceof String json) {
			return mapper.readValue(json, type);
		}
		else if (readVal instanceof Reader reader) {
			return mapper.readValue(reader, type);
		}
		else if (readVal instanceof InputStream inputStream) {
			return mapper.readValue(inputStream, type);
		}
		else if (readVal instanceof byte[] bytes) {
			return mapper.readValue(bytes, type);
		}
		throw new UnsupportedOperationException("Unsupported type: " + readVal.getClass().getName());
	}

	@SneakyThrows
	@Override
	public String writeValueAsString(Object writeVal) {
		if (writeVal instanceof String str) {
			return str;
		}
		return mapper.writeValueAsString(writeVal);
	}

	@SneakyThrows
	@Override
	public byte[] writeValueAsBytes(Object writeVal) {
		return mapper.writeValueAsBytes(writeVal);
	}

	@SneakyThrows
	@Override
	public JsonNode readTree(Object readVal) {
		if (readVal instanceof JsonParser jsonParser) {
			return mapper.readTree(jsonParser);
		}
		else if (readVal instanceof File file) {
			return mapper.readTree(file);
		}
		else if (readVal instanceof URL url) {
			return this.readTree(Paths.get(url.toURI()));
		}
		else if (readVal instanceof Path path) {
			return mapper.readTree(path);
		}
		else if (readVal instanceof String json) {
			return mapper.readTree(json);
		}
		else if (readVal instanceof Reader reader) {
			return mapper.readTree(reader);
		}
		else if (readVal instanceof InputStream inputStream) {
			return mapper.readTree(inputStream);
		}
		else if (readVal instanceof byte[] bytes) {
			return mapper.readTree(bytes);
		}
		return mapper.valueToTree(readVal);
	}

	@Override
	public <T> T convertValue(Object fromValue, JavaType javaType) {
		return mapper.convertValue(fromValue, javaType);
	}

	@Override
	public ObjectMapper unwrap() {
		return mapper;
	}

}
