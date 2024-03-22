/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.context.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.livk.commons.io.ResourceUtils;
import com.livk.commons.util.BaseStreamUtils;
import com.livk.context.easyexcel.annotation.ResponseExcel;
import com.livk.context.easyexcel.listener.ExcelMapReadListener;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The type Easy excel utils.
 */
@Slf4j
@UtilityClass
public class EasyExcelSupport {

	/**
	 * Read.
	 * @param in the in
	 * @param excelModelClass the excel model class
	 * @param listener the listener
	 * @param ignoreEmptyRow the ignore empty row
	 */
	public void read(InputStream in, Class<?> excelModelClass, ExcelMapReadListener<?> listener,
			Boolean ignoreEmptyRow) {
		try (ExcelReader excelReader = EasyExcel.read(in, listener).ignoreEmptyRow(ignoreEmptyRow).build()) {
			List<ReadSheet> readSheets = excelReader.excelExecutor()
				.sheetList()
				.stream()
				.map(sheet -> EasyExcel.readSheet(sheet.getSheetNo(), sheet.getSheetName())
					.head(excelModelClass)
					.build())
				.toList();
			excelReader.read(readSheets);
			excelReader.finish();
		}
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
		ExcelWriterBuilder builder = EasyExcel.write(outputStream);
		if (StringUtils.hasText(location)) {
			try {
				File file = ResourceUtils.getFile(location);
				builder.withTemplate(file);
				templateWrite(builder, result);
				return;
			}
			catch (FileNotFoundException e) {
				log.info("EasyExcel使用模板错误:{}", e.getMessage(), e);
			}
		}
		builder.head(excelModelClass);
		ordinaryWrite(EasyExcel.write(outputStream, excelModelClass), result);
	}

	private void ordinaryWrite(ExcelWriterBuilder builder, Map<String, ? extends Collection<?>> result) {
		try (ExcelWriter writer = builder.build()) {
			for (Map.Entry<String, ? extends Collection<?>> entry : result.entrySet()) {
				WriteSheet sheet = EasyExcel.writerSheet(entry.getKey()).build();
				writer.write(entry.getValue(), sheet);
			}
			writer.finish();
		}
	}

	private void templateWrite(ExcelWriterBuilder builder, Map<String, ? extends Collection<?>> result) {
		try (ExcelWriter writer = builder.build()) {
			result.entrySet().forEach(BaseStreamUtils.forEachWithIndex(0, (entry, index) -> {
				WriteSheet writeSheet = EasyExcel.writerSheet(index, entry.getKey())
					.registerWriteHandler(new SheetWriteHandler() {
						@Override
						public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
								WriteSheetHolder writeSheetHolder) {
							writeWorkbookHolder.getCachedWorkbook().setSheetName(index, entry.getKey());
						}
					})
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
	 */
	public String fileName(ResponseExcel excelReturn) {
		String template = excelReturn.template();
		String suffix;
		if (StringUtils.hasText(template)) {
			int index = template.lastIndexOf('.');
			suffix = template.substring(index);
		}
		else {
			suffix = excelReturn.suffix().getName();
		}
		return excelReturn.fileName().concat(suffix);
	}

}
