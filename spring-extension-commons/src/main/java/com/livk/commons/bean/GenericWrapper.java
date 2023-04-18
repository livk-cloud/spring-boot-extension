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

import org.springframework.core.GenericTypeResolver;

/**
 * The interface Delegating wrapper.
 *
 * @param <V> the type parameter
 * @author livk
 */
public interface GenericWrapper<V> extends Wrapper {

    /**
     * Of delegating wrapper.
     *
     * @param <V>   the type parameter
     * @param value the value
     * @return the delegating wrapper
     */
    static <V> GenericWrapper<V> of(V value) {
        return new RecordWrapper<>(value);
    }

    @Override
    default boolean isWrapperFor(Class<?> type) {
        Class<?> typeArgument = GenericTypeResolver.resolveTypeArgument(this.getClass(), GenericWrapper.class);
        return typeArgument == null || type.equals(typeArgument);
    }

    @Override
    default <T> T unwrap(Class<T> type) {
        return type.cast(unwrap());
    }

    /**
     * Unwrap t.
     *
     * @return the t
     */
    V unwrap();
}
