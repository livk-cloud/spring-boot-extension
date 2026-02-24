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

package com.livk.context.fesod.resolver;

import com.livk.commons.io.ResourceUtils;
import com.livk.context.fesod.ExcelDataType;
import com.livk.context.fesod.listener.ExcelMapReadListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.write.builder.ExcelWriterBuilder;
import org.apache.fesod.sheet.write.handler.SheetWriteHandler;
import org.apache.fesod.sheet.write.metadata.WriteSheet;
import org.apache.fesod.sheet.write.metadata.holder.WriteSheetHolder;
import org.apache.fesod.sheet.write.metadata.holder.WriteWorkbookHolder;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * The type Easy excel utils.
 */
@Slf4j
abstract class FesodSupport {

	/**
	 * Write.
	 * @param outputStream the output stream
	 * @param excelModelClass the excel model class
	 * @param location the location
	 * @param result the result
	 */
	void write(OutputStream outputStream, Class<?> excelModelClass, String location,
			Map<String, ? extends List<?>> result) throws IOException {
		ExcelWriterBuilder builder = FesodSheet.write(outputStream, excelModelClass);
		if (StringUtils.hasText(location)) {
			Resource resource = ResourceUtils.getResource(location);
			builder.withTemplate(resource.getInputStream());
		}
		try (ExcelWriter writer = builder.build()) {
			int index = 0;
			for (Map.Entry<String, ? extends List<?>> entry : result.entrySet()) {
				WriteSheet sheet = buildSheet(location, entry, index++);
				writer.write(entry.getValue(), sheet);
			}
		}
	}

	protected WriteSheet buildSheet(String location, Map.Entry<String, ? extends List<?>> entry, int index) {
		if (StringUtils.hasText(location)) {
			return FesodSheet.writerSheet(index, entry.getKey())
				.registerWriteHandler(new TemplateSheetWriteHandler(index, entry.getKey()))
				.build();
		}
		return FesodSheet.writerSheet(entry.getKey()).build();
	}

	<T> Object getExcelData(ExcelMapReadListener<T> listener, ExcelDataType type) {
		return switch (type) {
			case MAP -> listener.toMapData();
			case LIST -> listener.toListData();
		};
	}

	@RequiredArgsConstructor
	private static final class TemplateSheetWriteHandler implements SheetWriteHandler {

		private final Integer sheetIndex;

		private final String sheetName;

		@Override
		public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
			writeWorkbookHolder.getCachedWorkbook().setSheetName(sheetIndex, sheetName);
		}

	}

}
