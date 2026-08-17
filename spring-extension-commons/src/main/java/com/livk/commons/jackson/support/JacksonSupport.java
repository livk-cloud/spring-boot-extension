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
import tools.jackson.core.JsonParser;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;

/**
 * Default implementation of Jackson operations.
 *
 * @author livk
 */
public final class JacksonSupport extends AbstractJacksonOps implements JacksonOps, GenericWrapper<ObjectMapper> {

	private final ObjectMapper mapper;

	/**
	 * Constructs a JacksonSupport instance.
	 * @param mapper jacksonMapper
	 */
	public JacksonSupport(ObjectMapper mapper) {
		super(mapper.getTypeFactory());
		this.mapper = mapper;
	}

	@Override
	public <T> T readValue(Object readVal, JavaType type) {
		if (readVal instanceof JsonParser jsonParser) {
			return this.mapper.readValue(jsonParser, type);
		}
		if (readVal instanceof JsonNode jsonNode) {
			return this.mapper.treeToValue(jsonNode, type);
		}
		else if (readVal instanceof File file) {
			return this.mapper.readValue(file, type);
		}
		else if (readVal instanceof Path path) {
			return this.mapper.readValue(path, type);
		}
		else if (readVal instanceof String json) {
			return this.mapper.readValue(json, type);
		}
		else if (readVal instanceof Reader reader) {
			return this.mapper.readValue(reader, type);
		}
		else if (readVal instanceof InputStream inputStream) {
			return this.mapper.readValue(inputStream, type);
		}
		else if (readVal instanceof byte[] bytes) {
			return this.mapper.readValue(bytes, type);
		}
		throw new UnsupportedOperationException("Unsupported type: " + readVal.getClass().getName());
	}

	@Override
	public String writeValueAsString(Object writeVal) {
		if (writeVal instanceof String str) {
			return str;
		}
		return this.mapper.writeValueAsString(writeVal);
	}

	@Override
	public byte[] writeValueAsBytes(Object writeVal) {
		return this.mapper.writeValueAsBytes(writeVal);
	}

	@Override
	public JsonNode readTree(Object readVal) {
		if (readVal instanceof JsonParser jsonParser) {
			return this.mapper.readTree(jsonParser);
		}
		else if (readVal instanceof File file) {
			return this.mapper.readTree(file);
		}
		else if (readVal instanceof Path path) {
			return this.mapper.readTree(path);
		}
		else if (readVal instanceof String json) {
			return this.mapper.readTree(json);
		}
		else if (readVal instanceof Reader reader) {
			return this.mapper.readTree(reader);
		}
		else if (readVal instanceof InputStream inputStream) {
			return this.mapper.readTree(inputStream);
		}
		else if (readVal instanceof byte[] bytes) {
			return this.mapper.readTree(bytes);
		}
		throw new UnsupportedOperationException("Unsupported type: " + readVal.getClass().getName());
	}

	@Override
	public <T> T convertValue(Object fromValue, JavaType javaType) {
		return this.mapper.convertValue(fromValue, javaType);
	}

	@Override
	public ObjectMapper unwrap() {
		return this.mapper;
	}

}
