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

package com.livk.autoconfigure.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.livk.autoconfigure.easyexcel.utils.ExcelDataType;

import java.util.Collection;
import java.util.Map;

/**
 * The interface Excel map read listener.
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface ExcelMapReadListener<T> extends ReadListener<T> {

    @Override
    default void doAfterAllAnalysed(AnalysisContext context) {

    }

    /**
     * Get collection data collection.
     *
     * @return the collection
     */
    default Collection<T> getCollectionData() {
        return getMapData().values()
                .stream()
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * 获取数据集合
     *
     * @return collection collection data
     */
    Map<String, ? extends Collection<T>> getMapData();

    /**
     * Get data object.
     *
     * @param type the type
     * @return the object
     */
    default Object getData(ExcelDataType type) {
        return switch (type) {
            case MAP -> getMapData();
            case COLLECTION -> getCollectionData();
        };
    }
}
