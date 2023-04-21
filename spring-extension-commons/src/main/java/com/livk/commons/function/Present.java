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

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>
 * PresentOrElseHandler
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
@FunctionalInterface
public interface Present<T> {

    /**
     * Handler present.
     *
     * @param <T>       the type parameter
     * @param t         the t
     * @param predicate the predicate
     * @return the present
     */
    static <T> Present<T> handler(T t, Predicate<T> predicate) {
        return (action, emptyAction) -> {
            if (predicate.test(t))
                action.accept(t);
            else
                emptyAction.run();
        };
    }

    /**
     * Present.
     *
     * @param action      the action
     * @param emptyAction the empty action
     */
    void present(Consumer<T> action, Runnable emptyAction);

}
