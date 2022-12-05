package com.livk.excel.mvc.controller;

import com.livk.autoconfigure.excel.annotation.ExcelImport;
import com.livk.autoconfigure.excel.annotation.ExcelReturn;
import com.livk.excel.mvc.entity.Info;
import com.livk.excel.mvc.listener.InfoExcelListener;
import com.livk.excel.mvc.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * InfoController
 * </p>
 *
 * @author livk
 */
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @ExcelImport(parse = InfoExcelListener.class, paramName = "dataExcels")
    @PostMapping("upload")
    public HttpEntity<Boolean> upload(List<Info> dataExcels) {
        infoService.insertBatch(dataExcels);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @ExcelImport(parse = InfoExcelListener.class, paramName = "dataExcels")
    @PostMapping("uploadList")
    public HttpEntity<List<Info>> uploadList(List<Info> dataExcels) {
        return ResponseEntity.ok(dataExcels);
    }

    @ExcelReturn(fileName = "outFile")
    @ExcelImport(paramName = "dataExcels")
    @PostMapping("uploadAndDownload")
    public Map<String, List<Info>> uploadAndDownload(List<Info> dataExcels) {
        return dataExcels.stream()
                .collect(Collectors.groupingBy(info -> String.valueOf(Long.parseLong(info.getPhone()) % 10)));
    }

    @ExcelReturn(fileName = "outFile")
    @PostMapping("download")
    public Map<String, List<Info>> download(@RequestBody List<Info> dataExcels) {
        return dataExcels.stream()
                .collect(Collectors.groupingBy(info -> String.valueOf(Long.parseLong(info.getPhone()) % 10)));
    }
}
