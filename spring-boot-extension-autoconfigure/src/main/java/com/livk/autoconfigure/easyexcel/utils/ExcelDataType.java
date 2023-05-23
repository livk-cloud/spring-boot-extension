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

package com.livk.autoconfigure.easyexcel.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The enum Excel data type.
 *
 * @author livk
 */
@RequiredArgsConstructor
public enum ExcelDataType {
    /**
     * Collection excel data type.
     */
    COLLECTION(resolvableType -> resolvableType.resolveGeneric(0), Collection.class::isAssignableFrom),
    /**
     * Map excel data type.
     */
    MAP(resolvableType -> resolvableType.getGeneric(1).resolveGeneric(0), Map.class::isAssignableFrom);

    @Getter
    private final Function<ResolvableType, Class<?>> function;

    private final Predicate<Class<?>> assignableFrom;

    /**
     * Match excel data type.
     *
     * @param type the type
     * @return the excel data type
     */
    public static ExcelDataType match(Class<?> type) {
        for (final ExcelDataType excelDataType : values()) {
            if (excelDataType.assignableFrom.test(type)) {
                return excelDataType;
            }
        }
        throw new IllegalArgumentException();
    }
}
