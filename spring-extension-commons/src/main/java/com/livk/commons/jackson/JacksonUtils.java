/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.commons.bean.Wrapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import java.io.DataInput;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Jackson一些基本序列化与反序列化
 * 自定义Mapper，请注册{@link Wrapper}到IOC
 * <p>
 * 当前类设计存在语义不清
 * <p>
 * 请使用{@link JsonMapperUtils}
 *
 * @see com.livk.commons.jackson.support.JacksonSupport
 */
@Slf4j
@UtilityClass
@Deprecated(forRemoval = true)
public class JacksonUtils {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = JacksonWrapper.unwrapOfContext();
        MAPPER.registerModules(new JavaTimeModule());
    }

    /**
     * 获取当前MAPPER的TypeFactory
     *
     * @return the type factory
     */
    public static TypeFactory typeFactory() {
        return MAPPER.getTypeFactory();
    }

    /**
     * 构建一个JavaType
     *
     * @param targetClass the target class
     * @return the java type
     */
    public static JavaType javaType(Class<?> targetClass) {
        return MAPPER.getTypeFactory().constructType(targetClass);
    }

    /**
     * 构建一个含有泛型的JavaType
     *
     * @param targetClass the target class
     * @param generics    the generics
     * @return the java type
     */
    public static JavaType javaType(Class<?> targetClass, Class<?>... generics) {
        return MAPPER.getTypeFactory().constructParametricType(targetClass, generics);
    }

    /**
     * ResolvableType转成Jackson JavaType
     *
     * @param resolvableType the resolvable type
     * @return the java type
     * @see ResolvableType
     */
    public static JavaType javaType(ResolvableType resolvableType) {
        Class<?> rawClass = resolvableType.getRawClass();
        if (resolvableType.getType() instanceof ParameterizedType parameterizedType) {
            JavaType[] javaTypes = Arrays.stream(parameterizedType.getActualTypeArguments())
                    .map(type -> javaType(ResolvableType.forType(type)))
                    .toArray(JavaType[]::new);
            return typeFactory().constructParametricType(rawClass, javaTypes);
        }
        return javaType(rawClass);
    }

    /**
     * 构建一个CollectionType
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @return the java type
     * @see CollectionType
     */
    public static <T> CollectionType collectionType(Class<T> targetClass) {
        return MAPPER.getTypeFactory().constructCollectionType(Collection.class, targetClass);
    }

    /**
     * 构建一个MapType
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param keyClass   the key class
     * @param valueClass the value class
     * @return the java type
     * @see MapType
     */
    public static <K, V> MapType mapType(Class<K> keyClass, Class<V> valueClass) {
        return MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    @SneakyThrows
    public static <T> T readValue(Object obj, Class<T> type) {
        return readValue(obj, javaType(type));
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    @SneakyThrows
    public <T> T readValue(Object obj, JavaType type) {
        if (obj instanceof JsonParser jsonParser) {
            return MAPPER.readValue(jsonParser, type);
        } else if (obj instanceof File file) {
            return MAPPER.readValue(file, type);
        } else if (obj instanceof URL url) {
            return MAPPER.readValue(url, type);
        } else if (obj instanceof String json) {
            return MAPPER.readValue(json, type);
        } else if (obj instanceof Reader reader) {
            return MAPPER.readValue(reader, type);
        } else if (obj instanceof InputStream inputStream) {
            return MAPPER.readValue(inputStream, type);
        } else if (obj instanceof byte[] bytes) {
            return MAPPER.readValue(bytes, type);
        } else if (obj instanceof DataInput dataInput) {
            return MAPPER.readValue(dataInput, type);
        }
        return MAPPER.convertValue(obj, type);
    }

    /**
     * Read value t.
     *
     * @param <T>           the type parameter
     * @param obj           the obj
     * @param typeReference the type reference
     * @return the t
     */
    @SneakyThrows
    public static <T> T readValue(Object obj, TypeReference<T> typeReference) {
        return readValue(obj, MAPPER.getTypeFactory().constructType(typeReference));
    }

    /**
     * json序列化
     *
     * @param obj obj
     * @return json string
     */
    @SneakyThrows
    public static String writeValueAsString(Object obj) {
        if (obj instanceof String str) {
            return str;
        }
        return MAPPER.writeValueAsString(obj);
    }

    /**
     * Write value as bytes byte [ ].
     *
     * @param obj the obj
     * @return the byte [ ]
     */
    @SneakyThrows
    public static byte[] writeValueAsBytes(Object obj) {
        if (obj instanceof String str) {
            return str.getBytes();
        }
        return MAPPER.writeValueAsBytes(obj);
    }

    /**
     * json反序列化成List
     * <p>也可以看看{@link JacksonUtils#readValue(Object, TypeReference)} ,
     * <p> {@link JacksonUtils#convertValue(Object, JavaType)}
     *
     * @param <T>   泛型
     * @param obj   the obj
     * @param clazz 类型
     * @return the list
     */
    @SneakyThrows
    public static <T> List<T> readValueList(Object obj, Class<T> clazz) {
        CollectionType collectionType = collectionType(clazz);
        return readValue(obj, collectionType);
    }

    /**
     * json反序列化成Map
     * <p>也可以看看{@link JacksonUtils#readValue(Object, TypeReference)} ,
     * <p> {@link JacksonUtils#convertValue(Object, JavaType)}
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param obj        the obj
     * @param keyClass   K Class
     * @param valueClass V Class
     * @return the map
     */
    @SneakyThrows
    public static <K, V> Map<K, V> readValueMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
        MapType mapType = mapType(keyClass, valueClass);
        return readValue(obj, mapType);
    }

    /**
     * 将json转化成JsonNode
     *
     * @param obj the obj
     * @return the json node
     */
    @SneakyThrows
    public JsonNode readTree(Object obj) {
        if (obj instanceof JsonParser jsonParser) {
            return MAPPER.readTree(jsonParser);
        } else if (obj instanceof File file) {
            return MAPPER.readTree(file);
        } else if (obj instanceof URL url) {
            return MAPPER.readTree(url);
        } else if (obj instanceof String json) {
            return MAPPER.readTree(json);
        } else if (obj instanceof Reader reader) {
            return MAPPER.readTree(reader);
        } else if (obj instanceof InputStream inputStream) {
            return MAPPER.readTree(inputStream);
        } else if (obj instanceof byte[] bytes) {
            return MAPPER.readTree(bytes);
        }
        return null;
    }

    /**
     * Convert object.
     *
     * @param <T>       the type parameter
     * @param fromValue the  value
     * @param type      the type
     * @return the object
     */
    public static <T> T convertValue(Object fromValue, Class<T> type) {
        return MAPPER.convertValue(fromValue, type);
    }

    /**
     * Convert value t.
     *
     * @param <T>           the type parameter
     * @param fromValue     the value
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
        return MAPPER.convertValue(fromValue, typeReference);
    }

    /**
     * Convert object.
     *
     * @param <T>       the type parameter
     * @param fromValue the  value
     * @param javaType  the java type
     * @return the object
     */
    public static <T> T convertValue(Object fromValue, JavaType javaType) {
        return MAPPER.convertValue(fromValue, javaType);
    }

    /**
     * Object to map map.
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param fromValue  the fromValue
     * @param keyClass   the key class
     * @param valueClass the value class
     * @return the map
     */
    public static <K, V> Map<K, V> convertValueMap(Object fromValue, Class<K> keyClass, Class<V> valueClass) {
        MapType mapType = mapType(keyClass, valueClass);
        return MAPPER.convertValue(fromValue, mapType);
    }
}
