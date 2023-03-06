package com.livk.autoconfigure.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

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
}
