package com.livk.excel.controller;

import com.livk.autoconfigure.excel.annotation.ExcelImport;
import com.livk.autoconfigure.excel.annotation.ExcelReturn;
import com.livk.excel.entity.Info;
import com.livk.excel.listener.InfoExcelListener;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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

    @ExcelImport(parse = InfoExcelListener.class, paramName = "dataExcels")
    @PostMapping("upload")
    public HttpEntity<List<Info>> upload(List<Info> dataExcels) {
        return ResponseEntity.ok(dataExcels);
    }

    @ExcelImport(parse = InfoExcelListener.class, paramName = "dataExcels")
    @PostMapping("uploadMono")
    public Mono<HttpEntity<List<Info>>> uploadMono(Mono<List<Info>> dataExcels) {
        return dataExcels.map(ResponseEntity::ok);
    }

    @ExcelReturn(fileName = "outFile")
    @ExcelImport(parse = InfoExcelListener.class, paramName = "dataExcels")
    @PostMapping("uploadDownLoadMono")
    public List<Info> uploadDownLoadMono(List<Info> dataExcels) {
        return dataExcels;
    }
}
