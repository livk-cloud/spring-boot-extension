package com.livk.excel.batch.support;

import com.alibaba.excel.EasyExcel;
import com.livk.autoconfigure.easyexcel.listener.ExcelMapReadListener;
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
        Class<T> targetClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(excelReadListener.getClass(), ExcelMapReadListener.class);
        EasyExcel.read(inputStream, targetClass, excelReadListener)
                .sheet().doRead();
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
