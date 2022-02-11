package com.livk.excel.support;

import com.livk.excel.entity.ExcelInfo;
import com.livk.excel.entity.ExportMember;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * UserInfoExcelView
 * </p>
 *
 * @author livk
 * @date 2022/2/11
 */
public class UserInfoExcelView extends ExcelView<ExportMember> {

    public UserInfoExcelView(ExcelInfo<ExportMember> excelInfo) {
        super(excelInfo, new DefaultCellStyle());
    }

    @Override
    protected void setRow(Sheet sheet, Map<String, Object> model) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("姓名");
        header.getCell(0).setCellStyle(super.cellStyle);
        header.createCell(1).setCellValue("性别");
        header.getCell(1).setCellStyle(super.cellStyle);
        header.createCell(2).setCellValue("手机号");
        header.getCell(2).setCellStyle(super.cellStyle);
        header.createCell(3).setCellValue("身份证号");
        header.getCell(3).setCellStyle(super.cellStyle);
        header.createCell(4).setCellValue("银行卡号");
        header.getCell(4).setCellStyle(super.cellStyle);

        List<ExportMember> list = excelInfo.getData();
        int rowCount = 1;
        for (ExportMember user : list) {
            Row userRow = sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(user.getName());
            userRow.createCell(1).setCellValue(user.getGender());
            userRow.createCell(2).setCellValue(user.getPhone());
            userRow.createCell(3).setCellValue(user.getIdCard());
            userRow.createCell(4).setCellValue(user.getBankNo());
        }
    }
}
