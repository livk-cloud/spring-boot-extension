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

import cn.idev.excel.context.AnalysisContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author livk
 */
public final class TypeExcelMapReadListener<T> implements ExcelMapReadListener<T> {

	private final Map<String, List<T>> mapData = new HashMap<>();

	@Override
	public Map<String, ? extends List<T>> toMapData() {
		return mapData;
	}

	@Override
	public void invoke(T data, AnalysisContext context) {
		String sheetName = context.readSheetHolder().getSheetName();
		List<T> infos = mapData.computeIfAbsent(sheetName, s -> new ArrayList<>());
		infos.add(data);
	}

}
