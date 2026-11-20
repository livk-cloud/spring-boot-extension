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

package com.livk.context.fesod;

import com.livk.context.fesod.annotation.ExcelParam;
import com.livk.context.fesod.annotation.RequestExcel;
import com.livk.context.fesod.annotation.ResponseExcel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author livk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Info implements Serializable {

	@ExcelProperty(index = 0)
	private String phone;

	@SuppressWarnings("unused")
	@RequestExcel
	void resolveRequest(@ExcelParam List<Info> infos) {
	}

	@SuppressWarnings("unused")
	@RequestExcel
	void resolveRequestReactive(@ExcelParam Flux<Info> infos) {
	}

	@SuppressWarnings("unused")
	@RequestExcel
	void resolveRequestReactiveMono(@ExcelParam Mono<List<Info>> infos) {
	}

	@SuppressWarnings("unused")
	@RequestExcel
	void resolveRequestReactiveList(@ExcelParam List<Info> infos) {
	}

	@SuppressWarnings("unused")
	@RequestExcel
	void resolveRequestReactiveMonoMap(@ExcelParam Mono<Map<String, List<Info>>> infos) {
	}

	@SuppressWarnings("unused")
	void resolveWithoutAnnotations(@ExcelParam List<Info> infos) {
	}

	@SuppressWarnings("unused")
	@RequestExcel
	void resolveRequestUnsupported(@ExcelParam String data) {
	}

	@SuppressWarnings("unused")
	@RequestExcel
	void resolveRequestMap(@ExcelParam Map<String, List<Info>> data) {
	}

	@SuppressWarnings("unused")
	@ResponseExcel(fileName = "file")
	List<Info> resolveResponse() {
		return List.of();
	}

	@SuppressWarnings("unused")
	List<Info> resolveResponseWithoutAnnotation() {
		return List.of();
	}

	@SuppressWarnings("unused")
	@ResponseExcel(fileName = "file")
	Map<String, List<Info>> resolveResponseMap() {
		return Map.of();
	}

	@SuppressWarnings("unused")
	@ResponseExcel(fileName = "file")
	public Flux<Info> resolveResponseReactive() {
		return Flux.empty();
	}

	@SuppressWarnings("unused")
	@ResponseExcel(fileName = "file")
	public Mono<List<Info>> getMonoList() {
		return Mono.empty();
	}

	@SuppressWarnings("unused")
	@ResponseExcel(fileName = "file")
	public Mono<Map<String, List<Info>>> getMonoMap() {
		return Mono.empty();
	}

	@SuppressWarnings("unused")
	@ResponseExcel(fileName = "file")
	public Info unsupportedReturnType() {
		return null;
	}

}
