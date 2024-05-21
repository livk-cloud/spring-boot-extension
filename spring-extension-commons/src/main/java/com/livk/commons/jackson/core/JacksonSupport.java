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

package com.livk.commons.jackson.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.commons.beans.GenericWrapper;
import lombok.SneakyThrows;

import java.io.DataInput;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

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
		this.mapper.registerModules(new JavaTimeModule());
	}

	@SneakyThrows
	@Override
	public <T> T readValue(Object obj, JavaType type) {
		if (obj instanceof JsonParser jsonParser) {
			return mapper.readValue(jsonParser, type);
		}
		if (obj instanceof JsonNode jsonNode) {
			return mapper.treeToValue(jsonNode, type);
		}
		else if (obj instanceof File file) {
			return mapper.readValue(file, type);
		}
		else if (obj instanceof URL url) {
			return mapper.readValue(url, type);
		}
		else if (obj instanceof String json) {
			return mapper.readValue(json, type);
		}
		else if (obj instanceof Reader reader) {
			return mapper.readValue(reader, type);
		}
		else if (obj instanceof InputStream inputStream) {
			return mapper.readValue(inputStream, type);
		}
		else if (obj instanceof byte[] bytes) {
			return mapper.readValue(bytes, type);
		}
		else if (obj instanceof DataInput dataInput) {
			return mapper.readValue(dataInput, type);
		}
		throw new UnsupportedOperationException("Unsupported type: " + obj.getClass().getName());
	}

	@SneakyThrows
	@Override
	public String writeValueAsString(Object obj) {
		if (obj instanceof String str) {
			return str;
		}
		return mapper.writeValueAsString(obj);
	}

	@SneakyThrows
	@Override
	public byte[] writeValueAsBytes(Object obj) {
		return mapper.writeValueAsBytes(obj);
	}

	@SneakyThrows
	@Override
	public JsonNode readTree(Object obj) {
		if (obj instanceof JsonParser jsonParser) {
			return mapper.readTree(jsonParser);
		}
		else if (obj instanceof File file) {
			return mapper.readTree(file);
		}
		else if (obj instanceof URL url) {
			return mapper.readTree(url);
		}
		else if (obj instanceof String json) {
			return mapper.readTree(json);
		}
		else if (obj instanceof Reader reader) {
			return mapper.readTree(reader);
		}
		else if (obj instanceof InputStream inputStream) {
			return mapper.readTree(inputStream);
		}
		else if (obj instanceof byte[] bytes) {
			return mapper.readTree(bytes);
		}
		return mapper.valueToTree(obj);
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
