package com.livk.autoconfigure.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author livk
 */
public abstract class TypeExcelMapReadListener<T> implements ExcelMapReadListener<T> {

    private final Map<String, Collection<T>> mapData = new HashMap<>();

    @Override
    public Map<String, ? extends Collection<T>> getMapData() {
        return mapData;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        String sheetName = context.readSheetHolder().getSheetName();
        Collection<T> infos = mapData.computeIfAbsent(sheetName, s -> new ArrayList<>());
        infos.add(data);
    }
}
