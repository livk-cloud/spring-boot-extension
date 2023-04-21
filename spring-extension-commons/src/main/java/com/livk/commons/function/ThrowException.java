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

package com.livk.commons.function;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>
 * ThrowException
 * </p>
 *
 * @author livk
 */
@FunctionalInterface
public interface ThrowException {

    /**
     * Is true throw exception.
     *
     * @param <T>       the type parameter
     * @param obj       the obj
     * @param predicate the b
     * @return the throw exception
     */
    static <T> ThrowException isTrue(T obj, Predicate<T> predicate) {
        return (t) -> {
            if (predicate.test(obj))
                throw t;
        };
    }

    /**
     * Throw exception.
     *
     * @param t the message
     * @throws Throwable the throwable
     */
    void throwException(Throwable t) throws Throwable;

    /**
     * Throw exception.
     *
     * @param supplier the supplier
     * @throws Throwable the throwable
     */
    default void throwException(Supplier<Throwable> supplier) throws Throwable {
        Objects.requireNonNull(supplier);
        throwException(supplier.get());
    }

}
