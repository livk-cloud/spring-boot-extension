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

package com.livk.qrcode.webflux.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.core.qrcode.QRCodeEntity;
import com.livk.core.qrcode.annotation.ResponseQRCode;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>
 * QRCodeController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("qrcode")
public class QRCodeController {

	@ResponseQRCode
	@GetMapping
	public String text(String text) {
		return text;
	}

	@ResponseQRCode
	@GetMapping("mono")
	public Mono<String> textMono(String text) {
		return Mono.just(text);
	}

	@ResponseQRCode
	@GetMapping("entity")
	public QRCodeEntity<String> textCode(String text) {
		return QRCodeEntity.builder(text).build();
	}

	@ResponseQRCode
	@PostMapping("/entity/json")
	public QRCodeEntity<Map<String, String>> jsonCode(@RequestBody JsonNode node) {
		Map<String, String> map = JsonMapperUtils.convertValueMap(node, String.class, String.class);
		return QRCodeEntity.builder(map).build();
	}

	@ResponseQRCode
	@PostMapping("json")
	public Map<String, String> json(@RequestBody JsonNode node) {
		return JsonMapperUtils.convertValueMap(node, String.class, String.class);
	}

	@ResponseQRCode
	@PostMapping("/json/mono")
	public Mono<Map<String, String>> jsonMono(@RequestBody JsonNode node) {
		return Mono.just(JsonMapperUtils.convertValueMap(node, String.class, String.class));
	}

}
