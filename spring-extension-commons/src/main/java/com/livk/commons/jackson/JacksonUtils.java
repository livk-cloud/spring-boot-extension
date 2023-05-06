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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.livk.commons.bean.Wrapper;
import com.livk.commons.jackson.support.JacksonSupport;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

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

    /**
     * 获取当前MAPPER的TypeFactory
     *
     * @return the type factory
     */
    public static TypeFactory typeFactory() {
        return JacksonSupport.typeFactory();
    }

    /**
     * 构建一个JavaType
     *
     * @param targetClass the target class
     * @return the java type
     */
    public static JavaType javaType(Class<?> targetClass) {
        return JacksonSupport.javaType(targetClass);
    }

    /**
     * 构建一个含有泛型的JavaType
     *
     * @param targetClass the target class
     * @param generics    the generics
     * @return the java type
     */
    public static JavaType javaType(Class<?> targetClass, Class<?>... generics) {
        return JacksonSupport.javaType(targetClass, generics);
    }

    /**
     * ResolvableType转成Jackson JavaType
     *
     * @param resolvableType the resolvable type
     * @return the java type
     * @see ResolvableType
     */
    public static JavaType javaType(ResolvableType resolvableType) {
        return JacksonSupport.javaType(resolvableType);
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
        return JacksonSupport.collectionType(targetClass);
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
        return JacksonSupport.mapType(keyClass, valueClass);
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */

    public static <T> T readValue(Object obj, Class<T> type) {
        return JsonMapperUtils.readValue(obj, javaType(type));
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */

    public <T> T readValue(Object obj, JavaType type) {
        return JsonMapperUtils.readValue(obj, type);
    }

    /**
     * Read value t.
     *
     * @param <T>           the type parameter
     * @param obj           the obj
     * @param typeReference the type reference
     * @return the t
     */

    public static <T> T readValue(Object obj, TypeReference<T> typeReference) {
        return JsonMapperUtils.readValue(obj, typeReference);
    }

    /**
     * json序列化
     *
     * @param obj obj
     * @return json string
     */

    public static String writeValueAsString(Object obj) {
        return JsonMapperUtils.writeValueAsString(obj);
    }

    /**
     * Write value as bytes byte [ ].
     *
     * @param obj the obj
     * @return the byte [ ]
     */

    public static byte[] writeValueAsBytes(Object obj) {
        return JsonMapperUtils.writeValueAsBytes(obj);
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

    public static <T> List<T> readValueList(Object obj, Class<T> clazz) {
        return JsonMapperUtils.readValueList(obj, clazz);
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

    public static <K, V> Map<K, V> readValueMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
        return JsonMapperUtils.readValueMap(obj, keyClass, valueClass);
    }

    /**
     * 将json转化成JsonNode
     *
     * @param obj the obj
     * @return the json node
     */

    public JsonNode readTree(Object obj) {
        return JsonMapperUtils.readTree(obj);
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
        return JsonMapperUtils.convertValue(fromValue, type);
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
        return JsonMapperUtils.convertValue(fromValue, typeReference);
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
        return JsonMapperUtils.convertValue(fromValue, javaType);
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
        return JsonMapperUtils.convertValueMap(fromValue, keyClass, valueClass);
    }
}
