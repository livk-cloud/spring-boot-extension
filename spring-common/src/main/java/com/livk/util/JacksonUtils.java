package com.livk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * The type Jackson util.
 */
@Slf4j
@UtilityClass
public class JacksonUtils {

	/**
	 * The constant JSON_EMPTY.
	 */
	public static final String JSON_EMPTY = "{}";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * json字符转Bean
	 * @param json json string
	 * @param clazz class
	 * @param <T> type
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(String json, Class<T> clazz) {
		if (json == null || json.isEmpty() || clazz == null) {
			return null;
		}
		if (clazz.isInstance(json)) {
			return (T) json;
		}
		try {
			return MAPPER.readValue(json, clazz);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return BeanUtils.instantiateClass(clazz);
	}

	public static <T> T toBean(InputStream inputStream, Class<T> clazz) {
		if (inputStream == null || clazz == null) {
			return null;
		}
		try {
			return MAPPER.readValue(inputStream, clazz);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return BeanUtils.instantiateClass(clazz);
	}

	/**
	 * 序列化
	 * @param obj obj
	 * @return json
	 */
	public static String toJsonStr(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		}
		try {
			return MAPPER.writeValueAsString(obj);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return JSON_EMPTY;
	}

	/**
	 * json to List
	 * @param json json数组
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return List<T>
	 */
	public static <T> List<T> toList(String json, Class<T> clazz) {
		if (json == null || json.isEmpty() || clazz == null) {
			return new ArrayList<>();
		}
		CollectionType collectionType = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
		try {
			return MAPPER.readValue(json, collectionType);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * json反序列化Map
	 * @param json json字符串
	 * @param keyClass K Class
	 * @param valueClass V Class
	 * @return Map<K, V>
	 */
	public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
		if (!StringUtils.hasText(json) || keyClass == null || valueClass == null) {
			return Collections.emptyMap();
		}
		try {
			var mapType = MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
			return MAPPER.readValue(json, mapType);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyMap();
	}

	public Properties toProperties(InputStream inputStream) {
		if (inputStream == null) {
			return new Properties();
		}
		try {
			var mapType = MAPPER.getTypeFactory().constructType(Properties.class);
			return MAPPER.readValue(inputStream, mapType);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T toBean(String json, TypeReference<T> typeReference) {
		if (json == null || json.isEmpty() || typeReference == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, typeReference);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return BeanUtils.instantiateClass(ClassUtils.toClass(typeReference.getType()));
	}

}
