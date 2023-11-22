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

package com.livk.excel.batch.support;

import com.alibaba.excel.EasyExcel;
import com.livk.core.easyexcel.listener.ExcelMapReadListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.GenericTypeResolver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * EasyExcelItemReader
 * </p>
 *
 * @author livk
 */
public class EasyExcelItemReader<T> implements ItemReader<T> {

	private final List<T> data;

	@SuppressWarnings("unchecked")
	public EasyExcelItemReader(InputStream inputStream, ExcelMapReadListener<T> excelReadListener) {
		Class<T> targetClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(excelReadListener.getClass(),
				ExcelMapReadListener.class);
		EasyExcel.read(inputStream, targetClass, excelReadListener).sheet().doRead();
		data = new ArrayList<>(excelReadListener.getCollectionData());
	}

	@Override
	public T read() {
		if (!data.isEmpty()) {
			return data.remove(0);
		}
		return null;
	}

}
