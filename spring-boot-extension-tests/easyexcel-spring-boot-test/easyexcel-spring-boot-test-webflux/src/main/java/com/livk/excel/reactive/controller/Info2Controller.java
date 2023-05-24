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

package com.livk.excel.reactive.controller;

import com.livk.autoconfigure.easyexcel.annotation.ExcelController;
import com.livk.autoconfigure.easyexcel.annotation.ExcelImport;
import com.livk.autoconfigure.easyexcel.annotation.ExcelParam;
import com.livk.excel.reactive.entity.Info;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
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

    @ExcelImport
    @PostMapping("uploadAndDownload")
    public Map<String, List<Info>> uploadAndDownload(@ExcelParam Map<String, List<Info>> mapData) {
        return mapData.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(info -> String.valueOf(Long.parseLong(info.getPhone()) % 10)));
    }

    @PostMapping("download")
    public Map<String, List<Info>> download(@RequestBody List<Info> dataExcels) {
        return dataExcels.stream()
                .collect(Collectors.groupingBy(info -> String.valueOf(Long.parseLong(info.getPhone()) % 10)));
    }
}
