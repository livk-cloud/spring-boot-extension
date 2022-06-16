package com.livk.excel.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

import java.io.InputStream;
import java.util.Collection;

/**
 * <p>
 * ExcelReadListener
 * </p>
 *
 * @author livk
 * @date 2022/1/17
 */
public interface ExcelReadListener<T> extends ReadListener<T> {

	/**
	 * 获取数据集合
	 * @return collection
	 */
	Collection<T> getCollectionData();

	@Override
	default void doAfterAllAnalysed(AnalysisContext context) {

	}

	/**
	 * 解析数据
	 * @param inputStream 数据流
	 * @param targetClass 数据类型
	 * @return this
	 */
	default ExcelReadListener<T> parse(InputStream inputStream, Class<?> targetClass) {
		EasyExcel.read(inputStream, targetClass, this).sheet().doRead();
		return this;
	}

}
