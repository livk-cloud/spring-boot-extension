package com.livk.autoconfigure.easyexcel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.livk.autoconfigure.easyexcel.listener.ExcelReadListener;
import com.livk.commons.io.ResourceUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * ExcelUtils
 * </p>
 *
 * @author livk
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
     * @return the collection
     */
    public Collection<?> read(InputStream in, Class<?> excelModelClass, ExcelReadListener<?> listener, Boolean ignoreEmptyRow) {
        EasyExcel.read(in, excelModelClass, listener)
                .ignoreEmptyRow(ignoreEmptyRow)
                .sheet()
                .doRead();
        return listener.getCollectionData();
    }

    /**
     * Write.
     *
     * @param outputStream    the output stream
     * @param excelModelClass the excel model class
     * @param location        the location
     * @param result          the result
     */
    public void write(OutputStream outputStream, Class<?> excelModelClass, String location, Map<String, Collection<?>> result) {
        try (ExcelWriter writer = builder(outputStream, excelModelClass, location).build()) {
            for (Map.Entry<String, Collection<?>> entry : result.entrySet()) {
                WriteSheet sheet = EasyExcel.writerSheet(entry.getKey()).build();
                writer.write(entry.getValue(), sheet);
            }
            writer.finish();
        }
    }

    private ExcelWriterBuilder builder(OutputStream outputStream, Class<?> excelModelClass, String location) {
        if (StringUtils.hasText(location)) {
            Resource resource = ResourceUtils.getResource(location);
            if (resource.exists()) {
                try {
                    if (resource.isReadable()) {
                        return EasyExcel.write(outputStream).withTemplate(resource.getInputStream());
                    } else if (resource.isFile()) {
                        return EasyExcel.write(outputStream).withTemplate(resource.getFile());
                    }
                } catch (IOException e) {
                    log.info("EasyExcel使用模板错误:{}", e.getMessage(), e);
                }
            }
        }
        return EasyExcel.write(outputStream, excelModelClass);
    }
}
