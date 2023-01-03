package com.livk.excel.mvc.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.livk.autoconfigure.easyexcel.listener.ExcelReadListener;
import com.livk.excel.mvc.entity.Info;

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
