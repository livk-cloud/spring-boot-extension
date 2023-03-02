package com.livk.excel.reactive.controller;

import com.livk.autoconfigure.easyexcel.annotation.ExcelImport;
import com.livk.autoconfigure.easyexcel.annotation.ExcelParam;
import com.livk.autoconfigure.easyexcel.annotation.ExcelReturn;
import com.livk.excel.reactive.entity.Info;
import com.livk.excel.reactive.listener.InfoExcelListener;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @ExcelImport(parse = InfoExcelListener.class)
    @PostMapping("upload")
    public HttpEntity<List<Info>> upload(@ExcelParam List<Info> dataExcels) {
        return ResponseEntity.ok(dataExcels);
    }

    @ExcelImport(parse = InfoExcelListener.class)
    @PostMapping("uploadMono")
    public Mono<HttpEntity<List<Info>>> uploadMono(@ExcelParam Mono<List<Info>> dataExcels) {
        return dataExcels.map(ResponseEntity::ok);
    }

    @ExcelReturn(fileName = "outFile")
    @ExcelImport(parse = InfoExcelListener.class)
    @PostMapping("uploadDownLoad")
    public List<Info> uploadDownLoadMono(@ExcelParam List<Info> dataExcels) {
        return dataExcels;
    }

    @ExcelReturn(fileName = "outFile")
    @ExcelImport(parse = InfoExcelListener.class)
    @PostMapping("uploadDownLoadMono")
    public Mono<List<Info>> uploadDownLoadMono(@ExcelParam Mono<List<Info>> dataExcels) {
        return dataExcels;
    }

    @ExcelReturn(fileName = "outFile")
    @ExcelImport(parse = InfoExcelListener.class)
    @PostMapping("uploadDownLoadFlux")
    public Flux<Info> uploadDownLoadFlux(@ExcelParam Mono<List<Info>> dataExcels) {
        return dataExcels.flatMapMany(Flux::fromIterable);
    }
}
