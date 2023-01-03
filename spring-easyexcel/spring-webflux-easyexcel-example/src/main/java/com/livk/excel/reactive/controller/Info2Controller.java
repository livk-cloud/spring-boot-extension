package com.livk.excel.reactive.controller;

import com.livk.autoconfigure.easyexcel.annotation.ExcelController;
import com.livk.autoconfigure.easyexcel.annotation.ExcelImport;
import com.livk.excel.reactive.entity.Info;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * Info2Controller
 * </p>
 *
 * @author livk
 */
@ExcelController
@RequestMapping("info")
public class Info2Controller {

    @ExcelImport(paramName = "dataExcels")
    @PostMapping("uploadAndDownload")
    public Map<String, List<Info>> uploadAndDownload(List<Info> dataExcels) {
        return dataExcels.stream()
                .collect(Collectors.groupingBy(info -> String.valueOf(Long.parseLong(info.getPhone()) % 10)));
    }

    @PostMapping("download")
    public Map<String, List<Info>> download(@RequestBody List<Info> dataExcels) {
        return dataExcels.stream()
                .collect(Collectors.groupingBy(info -> String.valueOf(Long.parseLong(info.getPhone()) % 10)));
    }
}
