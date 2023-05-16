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

package com.livk.commons.bean;

/**
 * The interface Wrapper.
 */
public interface Wrapper {

    /**
     * Unwrap t.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the t
     */
    default <T> T unwrap(Class<T> type) {
        if (isWrapperFor(type)) {
            return type.cast(unwrap());
        }
        throw new ClassCastException("cannot be converted to " + type);
    }

    /**
     * Is wrapper for boolean.
     *
     * @param type the type
     * @return the boolean
     */
    boolean isWrapperFor(Class<?> type);

    /**
     * Unwrap object.
     *
     * @return the object
     */
    Object unwrap();
}
