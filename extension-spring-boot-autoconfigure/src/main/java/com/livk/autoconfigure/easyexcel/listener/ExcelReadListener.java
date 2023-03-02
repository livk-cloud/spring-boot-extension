package com.livk.autoconfigure.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

import java.util.Collection;

/**
 * <p>
 * ExcelReadListener
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface ExcelReadListener<T> extends ReadListener<T> {

    /**
     * 获取数据集合
     *
     * @return collection collection data
     */
    Collection<T> getCollectionData();

    @Override
    default void doAfterAllAnalysed(AnalysisContext context) {

    }
}
