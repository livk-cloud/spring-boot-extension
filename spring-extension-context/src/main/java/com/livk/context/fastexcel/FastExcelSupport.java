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

package com.livk.context.fastexcel;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.handler.SheetWriteHandler;
import cn.idev.excel.write.metadata.WriteSheet;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteWorkbookHolder;
import com.livk.commons.io.ResourceUtils;
import com.livk.commons.util.StreamUtils;
import com.livk.context.fastexcel.annotation.ResponseExcel;
import com.livk.context.fastexcel.listener.ExcelMapReadListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

/**
 * The type Easy excel utils.
 */
@Slf4j
public abstract class FastExcelSupport {

	/**
	 * Read.
	 * @param in the in
	 * @param excelModelClass the excel model class
	 * @param listener the listener
	 * @param ignoreEmptyRow the ignore empty row
	 * @deprecated use {@link ExcelMapReadListener#execute(InputStream, Class, Boolean)}
	 */
	@Deprecated(since = "1.4.5")
	public void read(InputStream in, Class<?> excelModelClass, ExcelMapReadListener<?> listener,
			Boolean ignoreEmptyRow) {
		listener.execute(in, excelModelClass, ignoreEmptyRow);
	}

	/**
	 * Write.
	 * @param outputStream the output stream
	 * @param excelModelClass the excel model class
	 * @param location the location
	 * @param result the result
	 */
	public void write(OutputStream outputStream, Class<?> excelModelClass, String location,
			Map<String, ? extends Collection<?>> result) {
		ExcelWriterBuilder builder = FastExcel.write(outputStream);
		if (StringUtils.hasText(location)) {
			try {
				Resource resource = ResourceUtils.getResource(location);
				builder.withTemplate(resource.getInputStream());
				templateWrite(builder, result);
			}
			catch (IOException ex) {
				log.info("FastExcel uses the template error:{}", ex.getMessage(), ex);
			}
		}
		else {
			builder.head(excelModelClass);
			ordinaryWrite(FastExcel.write(outputStream, excelModelClass), result);
		}
	}

	protected void ordinaryWrite(ExcelWriterBuilder builder, Map<String, ? extends Collection<?>> result) {
		try (ExcelWriter writer = builder.build()) {
			for (Map.Entry<String, ? extends Collection<?>> entry : result.entrySet()) {
				WriteSheet sheet = FastExcel.writerSheet(entry.getKey()).build();
				writer.write(entry.getValue(), sheet);
			}
			writer.finish();
		}
	}

	protected void templateWrite(ExcelWriterBuilder builder, Map<String, ? extends Collection<?>> result) {
		try (ExcelWriter writer = builder.build()) {
			result.entrySet().forEach(StreamUtils.forEachWithIndex(0, (entry, index) -> {
				WriteSheet writeSheet = FastExcel.writerSheet(index, entry.getKey())
					.registerWriteHandler(new TemplateSheetWriteHandler(index, entry.getKey()))
					.build();
				writer.write(entry.getValue(), writeSheet);
			}));
			writer.finish();
		}
	}

	/**
	 * File name string.
	 * @param excelReturn the excel return
	 * @return the string
	 * @deprecated use {@link ResponseExcel.Utils#parseName(ResponseExcel)}
	 */
	@Deprecated(since = "1.4.5")
	public String fileName(ResponseExcel excelReturn) {
		return ResponseExcel.Utils.parseName(excelReturn);
	}

	@RequiredArgsConstructor
	private static class TemplateSheetWriteHandler implements SheetWriteHandler {

		private final Integer sheetIndex;

		private final String sheetName;

		@Override
		public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
			writeWorkbookHolder.getCachedWorkbook().setSheetName(sheetIndex, sheetName);
		}

	}

}
