package com.livk.excel.reactive.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.google.common.collect.Lists;
import com.livk.autoconfigure.easyexcel.listener.ExcelReadListener;
import com.livk.excel.reactive.entity.Info;

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

    private final List<Info> dataExcels = Lists.newArrayList();

    @Override
    public void invoke(Info info, AnalysisContext context) {
        dataExcels.add(info);
    }

    @Override
    public Collection<Info> getCollectionData() {
        return dataExcels;
    }

}
