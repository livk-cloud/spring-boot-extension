package com.livk.autoconfigure.easyexcel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.livk.autoconfigure.easyexcel.annotation.ExcelReturn;
import com.livk.autoconfigure.easyexcel.listener.ExcelMapReadListener;
import com.livk.commons.collect.util.StreamUtils;
import com.livk.commons.io.ResourceUtils;
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
public class EasyExcelUtils {

    /**
     * Read.
     *
     * @param in              the in
     * @param excelModelClass the excel model class
     * @param listener        the listener
     * @param ignoreEmptyRow  the ignore empty row
     */
    public void read(InputStream in, Class<?> excelModelClass, ExcelMapReadListener<?> listener, Boolean ignoreEmptyRow) {
        try (ExcelReader excelReader = EasyExcel.read(in, listener)
                .ignoreEmptyRow(ignoreEmptyRow).build()) {
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
     *
     * @param outputStream    the output stream
     * @param excelModelClass the excel model class
     * @param location        the location
     * @param result          the result
     */
    public void write(OutputStream outputStream, Class<?> excelModelClass, String location, Map<String, ? extends Collection<?>> result) {
        ExcelWriterBuilder builder = EasyExcel.write(outputStream);
        if (StringUtils.hasText(location)) {
            try {
                File file = ResourceUtils.getFile(location);
                builder.withTemplate(file);
                templateWrite(builder, result);
                return;
            } catch (FileNotFoundException e) {
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
            result.entrySet().forEach(StreamUtils.forEachWithIndex(0, (entry, index) -> {
                WriteSheet writeSheet = EasyExcel.writerSheet(index, entry.getKey())
                        .registerWriteHandler(new SheetWriteHandler() {
                            @Override
                            public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
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
     *
     * @param excelReturn the excel return
     * @return the string
     */
    public String fileName(ExcelReturn excelReturn) {
        String template = excelReturn.template();
        String suffix;
        if (StringUtils.hasText(template)) {
            int index = template.lastIndexOf('.');
            suffix = template.substring(index);
        } else {
            suffix = excelReturn.suffix().getName();
        }
        return excelReturn.fileName().concat(suffix);
    }
}
