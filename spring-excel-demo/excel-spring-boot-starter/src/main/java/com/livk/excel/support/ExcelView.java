package com.livk.excel.support;

import com.livk.excel.entity.ExcelInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>
 * ExcelView
 * </p>
 *
 * @author livk
 * @date 2022/2/11
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ExcelView<T> extends AbstractXlsView {

    public CellStyle cellStyle;

    protected final ExcelInfo<T> excelInfo;

    public final AbstractCellStyle abstractCellStyle;

    private void setStyle(Workbook workbook) {
        cellStyle = abstractCellStyle.setCellStyle(workbook);
    }

    protected abstract void setRow(Sheet sheet, Map<String, Object> model);

    @Override
    protected void buildExcelDocument(@Nullable Map<String, Object> model, @Nullable Workbook workbook,
                                      HttpServletRequest request,
                                      @Nullable HttpServletResponse response) {
        Assert.notNull(response, "response not be null!");
        Assert.notNull(workbook, "workbook not be null!");
        String excelName = excelInfo.getExcelName() + ".xls";
        String agent = request.getHeader("User-Agent");
        if (null != agent) {
            agent = agent.toLowerCase();
            if (agent.contains("firefox")) {
                response.setHeader("content-disposition", String.format("attachment;filename*=utf-8'zh_cn'%s", URLEncoder.encode(excelName, StandardCharsets.UTF_8)));
            } else {
                response.setHeader("content-disposition", "attachment;filename=".concat(URLEncoder.encode(excelName, StandardCharsets.UTF_8)));
            }
        }
        response.setContentType("application/ms-excel; charset=UTF-8");
        Sheet sheet = workbook.createSheet("User Detail");
        sheet.setDefaultColumnWidth(30);
        this.setStyle(workbook);
        setRow(sheet, model);
    }
}
