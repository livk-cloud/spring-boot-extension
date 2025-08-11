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

package com.livk.excel.batch.support;

import cn.idev.excel.FastExcel;
import com.livk.commons.util.ClassUtils;
import com.livk.context.fastexcel.listener.ExcelMapReadListener;
import org.springframework.batch.item.ItemReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 */
public class FastExcelItemReader<T> implements ItemReader<T> {

	private final List<T> data;

	public FastExcelItemReader(InputStream inputStream, ExcelMapReadListener<T> excelReadListener) {
		Class<T> targetClass = ClassUtils.resolveTypeArgument(excelReadListener.getClass(), ExcelMapReadListener.class);
		FastExcel.read(inputStream, targetClass, excelReadListener).sheet().doRead();
		data = new ArrayList<>(excelReadListener.getCollectionData());
	}

	@Override
	public T read() {
		if (!data.isEmpty()) {
			return data.removeFirst();
		}
		return null;
	}

}
