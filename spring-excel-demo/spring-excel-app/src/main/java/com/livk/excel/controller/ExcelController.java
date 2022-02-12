package com.livk.excel.controller;

import com.livk.excel.entity.ExcelInfo;
import com.livk.excel.entity.ExportMember;
import com.livk.excel.support.ExcelView;
import com.livk.excel.support.UserInfoExcelView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * ExcelController
 * </p>
 *
 * @author livk
 * @date 2022/2/11
 */
@RestController
public class ExcelController {

    @GetMapping("excel")
    public ModelAndView download() {
        List<ExportMember> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ExportMember exportMemberVo = new ExportMember();
            exportMemberVo.setName("Kent" + i);
            int gender = ThreadLocalRandom.current().nextInt(0, 2);
            exportMemberVo.setGender(gender);
            exportMemberVo.setPhone("182xxxxxxxx");
            exportMemberVo.setBankName("建设银行");
            exportMemberVo.setBankNo("413543xxxxxxxxx");
            exportMemberVo.setIdCard("430xxxxxxxxxxxxxxxxx");
            list.add(exportMemberVo);
        }
        ExcelView<ExportMember> excelView = new UserInfoExcelView(ExcelInfo.of("魅力城市", list));
        return new ModelAndView(excelView);
    }
}
