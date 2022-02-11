package com.livk.excel.support;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * <p>
 * DefaultCellStyle
 * </p>
 *
 * @author livk
 * @date 2022/2/11
 */
public interface AbstractCellStyle {
    CellStyle setCellStyle(Workbook workbook);
}
