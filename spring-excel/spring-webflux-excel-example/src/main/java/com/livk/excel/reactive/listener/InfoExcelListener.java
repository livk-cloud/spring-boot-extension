package com.livk.excel.reactive.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.livk.autoconfigure.excel.listener.ExcelReadListener;
import com.livk.excel.reactive.entity.Info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * ExcelListener
 * </p>
 *
 * @author livk
 * @date 2021/11/18
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
