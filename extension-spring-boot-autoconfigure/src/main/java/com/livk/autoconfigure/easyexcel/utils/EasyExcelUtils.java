package com.livk.autoconfigure.easyexcel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.livk.autoconfigure.easyexcel.listener.ExcelReadListener;
import lombok.experimental.UtilityClass;

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
    public void read(InputStream in, Class<?> excelModelClass, ExcelReadListener<?> listener, Boolean ignoreEmptyRow) {
        EasyExcel.read(in, excelModelClass, listener)
                .ignoreEmptyRow(ignoreEmptyRow)
                .sheet()
                .doRead();
    }

    /**
     * Write.
     *
     * @param outputStream    the output stream
     * @param excelModelClass the excel model class
     * @param result          the result
     */
    public void write(OutputStream outputStream, Class<?> excelModelClass, Map<String, Collection<?>> result) {
        try (ExcelWriter writer = EasyExcel.write(outputStream, excelModelClass).build()) {
            for (Map.Entry<String, Collection<?>> entry : result.entrySet()) {
                WriteSheet sheet = EasyExcel.writerSheet(entry.getKey()).build();
                writer.write(entry.getValue(), sheet);
            }
        }
    }
}
