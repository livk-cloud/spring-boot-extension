/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.fastexcel.listener;

import cn.idev.excel.ExcelReader;
import cn.idev.excel.FastExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import cn.idev.excel.read.metadata.ReadSheet;
import com.livk.context.fastexcel.ExcelDataType;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
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
	 * @return the collection
	 */
	default Collection<T> getCollectionData() {
		return getMapData().values().stream().flatMap(Collection::stream).toList();
	}

	/**
	 * 获取数据集合
	 * @return collection collection data
	 */
	Map<String, ? extends Collection<T>> getMapData();

	/**
	 * Get data object.
	 * @param type the type
	 * @return the object
	 */
	default Object getData(ExcelDataType type) {
		return switch (type) {
			case MAP -> getMapData();
			case COLLECTION -> getCollectionData();
		};
	}

	default void execute(InputStream in, Class<?> excelModelClass, Boolean ignoreEmptyRow) {
		try (ExcelReader excelReader = FastExcel.read(in, this).ignoreEmptyRow(ignoreEmptyRow).build()) {
			List<ReadSheet> readSheets = excelReader.excelExecutor()
				.sheetList()
				.stream()
				.map(sheet -> FastExcel.readSheet(sheet.getSheetNo(), sheet.getSheetName())
					.head(excelModelClass)
					.build())
				.toList();
			excelReader.read(readSheets);
			excelReader.finish();
		}
	}

}
