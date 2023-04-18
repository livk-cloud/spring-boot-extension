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

import java.util.function.Predicate;

/**
 * <p>
 * BranchHandle
 * </p>
 *
 * @author livk
 */
@FunctionalInterface
public interface BranchHandle {

    /**
     * Is true or false branch handle.
     *
     * @param <T>       the type parameter
     * @param obj       the obj
     * @param predicate the predicate
     * @return the branch handle
     */
    static <T> BranchHandle isTrueOrFalse(T obj, Predicate<T> predicate) {
        return (trueHandle, falseHandle) -> {
            if (predicate.test(obj))
                trueHandle.run();
            else
                falseHandle.run();
        };
    }

    /**
     * True or false handle.
     *
     * @param trueHandle  the true handle
     * @param falseHandle the false handle
     */
    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);

}
