package com.livk.excel.controller;

import com.livk.excel.annotation.ExcelImport;
import com.livk.excel.annotation.ExcelReturn;
import com.livk.excel.entity.Info;
import com.livk.excel.listener.InfoExcelListener;
import com.livk.excel.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * InfoController
 * </p>
 *
 * @author livk
 * @date 2022/1/12
 */
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @ExcelImport(parse = InfoExcelListener.class, fileName = "file", paramName = "dataExcels")
    @PostMapping("upload")
    public HttpEntity<Boolean> upload( List<Info> dataExcels) {
        infoService.insertBatch(dataExcels);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @ExcelReturn(fileName = "outFile")
    @ExcelImport(fileName = "file", paramName = "dataExcels")
    @PostMapping("uploadAndDownload")
    public List<Info> uploadAndDownload(List<Info> dataExcels) {
        return dataExcels;
    }
}
