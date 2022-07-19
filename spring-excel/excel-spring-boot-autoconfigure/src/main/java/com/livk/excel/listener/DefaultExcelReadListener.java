package com.livk.excel.listener;

import com.alibaba.excel.context.AnalysisContext;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * DefaultExcelReadListener
 * </p>
 *
 * @author livk
 * @date 2022/2/14
 */
public class DefaultExcelReadListener implements ExcelReadListener<Object> {

    private final Collection<Object> dataExcels = new ArrayList<>();

    @Override
    public void invoke(Object info, AnalysisContext context) {
        dataExcels.add(info);
    }

    @Override
    public Collection<Object> getCollectionData() {
        return dataExcels;
    }

}
