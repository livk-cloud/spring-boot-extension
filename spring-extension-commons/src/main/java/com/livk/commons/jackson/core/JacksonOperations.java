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

package com.livk.commons.jackson.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.util.TypeFactoryUtils;

/**
 * @author livk
 */
public interface JacksonOperations {

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    default <T> T readValue(Object obj, Class<T> type) {
        return readValue(obj, TypeFactoryUtils.javaType(type));
    }

    /**
     * Read value t.
     *
     * @param <T>           the type parameter
     * @param obj           the obj
     * @param typeReference the type reference
     * @return the t
     */
    default <T> T readValue(Object obj, TypeReference<T> typeReference) {
        return readValue(obj, TypeFactoryUtils.javaType(typeReference));
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    <T> T readValue(Object obj, JavaType type);

    /**
     * json序列化
     *
     * @param obj obj
     * @return json string
     */
    String writeValueAsString(Object obj);

    /**
     * Write value as bytes byte [ ].
     *
     * @param obj the obj
     * @return the byte [ ]
     */

    byte[] writeValueAsBytes(Object obj);

    /**
     * 将json转化成JsonNode
     *
     * @param obj the obj
     * @return the json node
     */
    JsonNode readTree(Object obj);

    /**
     * Convert object.
     *
     * @param <T>       the type parameter
     * @param fromValue the  value
     * @param type      the type
     * @return the object
     */
    default <T> T convertValue(Object fromValue, Class<T> type) {
        return convertValue(fromValue, TypeFactoryUtils.javaType(type));
    }

    /**
     * Convert value t.
     *
     * @param <T>           the type parameter
     * @param fromValue     the value
     * @param typeReference the type reference
     * @return the t
     */
    default <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
        return convertValue(fromValue, TypeFactoryUtils.javaType(typeReference));
    }

    /**
     * Convert object.
     *
     * @param <T>       the type parameter
     * @param fromValue the  value
     * @param javaType  the java type
     * @return the object
     */
    <T> T convertValue(Object fromValue, JavaType javaType);
}
