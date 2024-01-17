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

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Streams;
import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;

import java.util.*;

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

	/**
	 * 获取json字符串的第一个子节点 从json串中自顶向下依次查找第一个出现的节点 {example { "c": "1", "a": "2", "b": {"c":
	 * 3} } getNodeFirst(json,"c")==>1
	 * <p>
	 * { "c": "1", "a": "2", "b": {"c": 3,"d":4}, "d": 5 } getNodeFirst(json,"d")==>4}
	 * @param jsonNode json
	 * @param nodeName node
	 * @return str node first
	 * @see JsonNode#at(String)
	 * @see JsonNode#at(JsonPointer)
	 */
	public JsonNode findNodeFirst(JsonNode jsonNode, String nodeName) {
		if (jsonNode == null || ObjectUtils.isEmpty(nodeName)) {
			return null;
		}
		if (jsonNode.isArray()) {
			Iterator<JsonNode> elements = jsonNode.elements();
			while (elements.hasNext()) {
				JsonNode result = findNodeFirst(elements.next(), nodeName);
				if (result != null) {
					return result;
				}
			}
		}
		else {
			Iterator<String> iterator = jsonNode.fieldNames();
			while (iterator.hasNext()) {
				String node = iterator.next();
				if (node.equals(nodeName)) {
					return jsonNode.get(nodeName);
				}
				else {
					JsonNode child = jsonNode.get(node);
					if (child.isContainerNode()) {
						JsonNode result = findNodeFirst(child, nodeName);
						if (result != null) {
							return result;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取所有节点为nodeName的数据
	 * @param jsonNode json
	 * @param nodeName name
	 * @return list
	 */
	public List<JsonNode> findNodeAll(JsonNode jsonNode, String nodeName) {
		if (jsonNode == null || ObjectUtils.isEmpty(nodeName)) {
			return Collections.emptyList();
		}
		List<JsonNode> jsonNodeList = new ArrayList<>();
		if (jsonNode.isArray()) {
			Iterator<JsonNode> elements = jsonNode.elements();
			while (elements.hasNext()) {
				List<JsonNode> firstAll = findNodeAll(elements.next(), nodeName);
				jsonNodeList.addAll(firstAll);
			}
		}
		else {
			Iterator<String> iterator = jsonNode.fieldNames();
			while (iterator.hasNext()) {
				String node = iterator.next();
				if (node.equals(nodeName)) {
					jsonNodeList.add(jsonNode.get(nodeName));
				}
				else {
					JsonNode child = jsonNode.get(node);
					if (child.isContainerNode()) {
						List<JsonNode> firstAll = findNodeAll(child, nodeName);
						jsonNodeList.addAll(firstAll);
					}
				}
			}
		}
		return jsonNodeList;
	}

	/**
	 * 获取json字符串的节点 {example { "c": "1", "a": "2", "b": { "c": 3, "d": { "ab": 6 } } }
	 * getNode(json, "b"))==>{"c":3,"d":{"ab":6}} getNode(json, "b.c"))==>3 getNode(json,
	 * "b.d"))==>{"ab":6} getNode(json, "b.d.ab"))==>6 getNode(json, "d"))==>null}
	 * @param jsonNode json
	 * @param nodePath node(节点之间以.隔开)
	 * @return node node
	 * @see JsonNode#at(String)
	 * @see JsonNode#at(JsonPointer)
	 */
	public JsonNode findNode(JsonNode jsonNode, String nodePath) {
		if (jsonNode == null || ObjectUtils.isEmpty(nodePath)) {
			return null;
		}
		int index = nodePath.indexOf('.');
		if (jsonNode.isArray()) {
			int range = Integer.parseInt(nodePath.substring(0, index));
			Iterator<JsonNode> elements = jsonNode.elements();
			JsonNode node = Streams.stream(elements).toList().get(range);
			return findNode(node, nodePath.substring(index + 1));
		}
		else {
			Iterator<String> iterator = jsonNode.fieldNames();
			while (iterator.hasNext()) {
				String node = iterator.next();
				if (index <= 0) {
					if (node.equals(nodePath)) {
						return jsonNode.get(nodePath);
					}
				}
				else {
					String parentNode = nodePath.substring(0, index);
					if (node.equals(parentNode)) {
						JsonNode child = jsonNode.get(node);
						if (child.isContainerNode()) {
							String childNode = nodePath.substring(index + 1);
							JsonNode result = findNode(child, childNode);
							if (result != null) {
								return result;
							}
						}
					}
				}
			}
		}
		return null;
	}

}
