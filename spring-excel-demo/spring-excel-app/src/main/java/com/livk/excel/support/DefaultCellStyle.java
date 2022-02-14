package com.livk.excel.support;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * <p>
 * DefaultCellStyle
 * </p>
 *
 * @author livk
 * @date 2022/2/11
 */
public class DefaultCellStyle implements AbstractCellStyle {
    @Override
    public CellStyle setCellStyle(Workbook workbook) {
        // create style for header cells
        var cellStyle = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setFontName("Arial");
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        cellStyle.setFont(font);
        return cellStyle;
    }
}
