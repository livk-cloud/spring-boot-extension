/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.excel.reactive.controller;

import com.livk.context.fastexcel.annotation.ExcelParam;
import com.livk.context.fastexcel.annotation.RequestExcel;
import com.livk.context.fastexcel.annotation.ResponseExcel;
import com.livk.excel.reactive.entity.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author livk
 */
@RestController
@RequiredArgsConstructor
public class InfoController {

	@RequestExcel
	@PostMapping("upload")
	public HttpEntity<List<Info>> upload(@ExcelParam List<Info> dataExcels) {
		return ResponseEntity.ok(dataExcels);
	}

	@RequestExcel
	@PostMapping("uploadMono")
	public Mono<HttpEntity<List<Info>>> uploadMono(@ExcelParam Mono<List<Info>> dataExcels) {
		return dataExcels.map(ResponseEntity::ok);
	}

	@ResponseExcel(fileName = "outFile")
	@RequestExcel
	@PostMapping("uploadDownLoad")
	public List<Info> uploadDownLoadMono(@ExcelParam List<Info> dataExcels) {
		return dataExcels;
	}

	@ResponseExcel(fileName = "outFile")
	@RequestExcel
	@PostMapping("uploadDownLoadMono")
	public Mono<List<Info>> uploadDownLoadMono(@ExcelParam Mono<List<Info>> dataExcels) {
		return dataExcels;
	}

	@ResponseExcel(fileName = "outFile")
	@RequestExcel
	@PostMapping("uploadDownLoadFlux")
	public Flux<Info> uploadDownLoadFlux(@ExcelParam Mono<List<Info>> dataExcels) {
		return dataExcels.flatMapMany(Flux::fromIterable);
	}

}
