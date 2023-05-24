/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.excel.mvc.controller;

import com.google.common.collect.Lists;
import com.livk.autoconfigure.easyexcel.annotation.ExcelImport;
import com.livk.autoconfigure.easyexcel.annotation.ExcelParam;
import com.livk.autoconfigure.easyexcel.annotation.ExcelReturn;
import com.livk.excel.mvc.entity.Info;
import com.livk.excel.mvc.listener.InfoExcelListener;
import com.livk.excel.mvc.service.InfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
@Slf4j
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @ExcelImport(parse = InfoExcelListener.class)
    @PostMapping("upload")
    public HttpEntity<Boolean> upload(@ExcelParam List<Info> dataExcels,
                                      @RequestParam(defaultValue = "false") Boolean async,
                                      @RequestParam(defaultValue = "1") Integer multiple) {
        List<Info> infos = this.multiple(dataExcels, multiple);
        log.info("size:{},async:{},multiple:{}", infos.size(), async, multiple);
        if (async) {
            infoService.insertBatchMultithreading(infos);
        } else {
            infoService.insertBatch(infos);
        }
        return ResponseEntity.ok(Boolean.TRUE);
    }

    private <T> List<T> multiple(List<T> list, int multiple) {
        List<T> ts = Lists.newArrayList();
        for (int i = 0; i < multiple; i++) {
            ts.addAll(Lists.newArrayList(list));
        }
        return ts;
    }

    @ExcelImport(parse = InfoExcelListener.class)
    @PostMapping("uploadList")
    public HttpEntity<List<Info>> uploadList(@ExcelParam List<Info> dataExcels) {
        return ResponseEntity.ok(dataExcels);
    }

    @ExcelReturn(fileName = "outFile")
    @ExcelImport
    @PostMapping("uploadAndDownload")
    public Map<String, List<Info>> uploadAndDownload(@ExcelParam List<Info> dataExcels) {
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
