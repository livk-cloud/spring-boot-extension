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

package com.livk.commons.jackson.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * JsonNodeUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class JsonNodeUtils {

	/**
	 * The String set.
	 */
	public final TypeReference<Set<String>> STRING_SET = new TypeReference<>() {
	};

	/**
	 * The String object map.
	 */
	public final TypeReference<Map<String, Object>> STRING_OBJECT_MAP = new TypeReference<>() {
	};

	/**
	 * 查找某个节点，转成string
	 * @param jsonNode the json node
	 * @param fieldName the field name
	 * @return the string
	 */
	public String findStringValue(JsonNode jsonNode, String fieldName) {
		if (jsonNode == null) {
			return null;
		}
		JsonNode value = jsonNode.findValue(fieldName);
		return (value != null && value.isTextual()) ? value.asText() : null;
	}

	/**
	 * 查找某个节点
	 * @param jsonNode the json node
	 * @param fieldName the field name
	 * @return the json node
	 */
	public JsonNode findObjectNode(JsonNode jsonNode, String fieldName) {
		if (jsonNode == null) {
			return null;
		}
		JsonNode value = jsonNode.findValue(fieldName);
		return (value != null && value.isObject()) ? value : null;
	}

	/**
	 * 查找某个节点
	 * @param <T> the type parameter
	 * @param jsonNode the json node
	 * @param fieldName the field name
	 * @param valueTypeReference the value type reference
	 * @param mapper the mapper
	 * @return the t
	 */
	public <T> T findValue(JsonNode jsonNode, String fieldName, TypeReference<T> valueTypeReference,
			ObjectMapper mapper) {
		JavaType javaType = TypeFactoryUtils.javaType(valueTypeReference);
		return findValue(jsonNode, fieldName, javaType, mapper);
	}

	/**
	 * 查找某个节点
	 * @param <T> the type parameter
	 * @param jsonNode the json node
	 * @param fieldName the field name
	 * @param type the type
	 * @param mapper the mapper
	 * @return the t
	 */
	public <T> T findValue(JsonNode jsonNode, String fieldName, Class<T> type, ObjectMapper mapper) {
		JavaType javaType = TypeFactoryUtils.javaType(type);
		return findValue(jsonNode, fieldName, javaType, mapper);
	}

	/**
	 * 查找某个节点
	 * @param <T> the type parameter
	 * @param jsonNode the json node
	 * @param fieldName the field name
	 * @param javaType the java type
	 * @param mapper the mapper
	 * @return T t
	 */
	public <T> T findValue(JsonNode jsonNode, String fieldName, JavaType javaType, ObjectMapper mapper) {
		if (jsonNode == null) {
			return null;
		}
		JsonNode value = jsonNode.findValue(fieldName);
		return (value != null && value.isContainerNode()) ? mapper.convertValue(value, javaType) : null;
	}

}
