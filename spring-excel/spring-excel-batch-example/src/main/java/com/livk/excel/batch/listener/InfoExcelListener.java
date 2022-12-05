package com.livk.excel.batch.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.livk.autoconfigure.excel.listener.ExcelReadListener;
import com.livk.excel.batch.entity.Info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * ExcelListener
 * </p>
 *
 * @author livk
 */
public class InfoExcelListener implements ExcelReadListener<Info> {

    private final List<Info> dataExcels = new ArrayList<>();

    @Override
    public void invoke(Info info, AnalysisContext context) {
        dataExcels.add(info);
    }

    @Override
    public Collection<Info> getCollectionData() {
        return dataExcels;
    }

}
