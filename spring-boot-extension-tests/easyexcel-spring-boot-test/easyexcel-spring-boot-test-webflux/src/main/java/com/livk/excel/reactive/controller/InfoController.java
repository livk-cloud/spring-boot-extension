/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.excel.reactive.controller;

import com.livk.core.easyexcel.annotation.ExcelParam;
import com.livk.core.easyexcel.annotation.RequestExcel;
import com.livk.core.easyexcel.annotation.ResponseExcel;
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

	@RequestExcel(parse = InfoExcelListener.class)
	@PostMapping("upload")
	public HttpEntity<List<Info>> upload(@ExcelParam List<Info> dataExcels) {
		return ResponseEntity.ok(dataExcels);
	}

	@RequestExcel(parse = InfoExcelListener.class)
	@PostMapping("uploadMono")
	public Mono<HttpEntity<List<Info>>> uploadMono(@ExcelParam Mono<List<Info>> dataExcels) {
		return dataExcels.map(ResponseEntity::ok);
	}

	@ResponseExcel(fileName = "outFile")
	@RequestExcel(parse = InfoExcelListener.class)
	@PostMapping("uploadDownLoad")
	public List<Info> uploadDownLoadMono(@ExcelParam List<Info> dataExcels) {
		return dataExcels;
	}

	@ResponseExcel(fileName = "outFile")
	@RequestExcel(parse = InfoExcelListener.class)
	@PostMapping("uploadDownLoadMono")
	public Mono<List<Info>> uploadDownLoadMono(@ExcelParam Mono<List<Info>> dataExcels) {
		return dataExcels;
	}

	@ResponseExcel(fileName = "outFile")
	@RequestExcel(parse = InfoExcelListener.class)
	@PostMapping("uploadDownLoadFlux")
	public Flux<Info> uploadDownLoadFlux(@ExcelParam Mono<List<Info>> dataExcels) {
		return dataExcels.flatMapMany(Flux::fromIterable);
	}

}
