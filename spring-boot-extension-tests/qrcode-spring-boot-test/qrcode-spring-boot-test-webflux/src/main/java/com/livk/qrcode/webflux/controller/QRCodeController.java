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

package com.livk.qrcode.webflux.controller;

import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.context.qrcode.QrCodeEntity;
import com.livk.context.qrcode.annotation.ResponseQrCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

import java.util.Map;

/**
 * @author livk
 */
@RestController
@RequestMapping("qrcode")
public class QRCodeController {

	@ResponseQrCode
	@GetMapping
	public String text(String text) {
		return text;
	}

	@ResponseQrCode
	@GetMapping("mono")
	public Mono<String> textMono(String text) {
		return Mono.just(text);
	}

	@ResponseQrCode
	@GetMapping("entity")
	public QrCodeEntity<String> textCode(String text) {
		return QrCodeEntity.builder(text).build();
	}

	@ResponseQrCode
	@PostMapping("/entity/json")
	public QrCodeEntity<Map<String, String>> jsonCode(@RequestBody JsonNode node) {
		Map<String, String> map = JsonMapperUtils.convertValueMap(node, String.class, String.class);
		return QrCodeEntity.builder(map).build();
	}

	@ResponseQrCode
	@PostMapping("json")
	public Map<String, String> json(@RequestBody JsonNode node) {
		return JsonMapperUtils.convertValueMap(node, String.class, String.class);
	}

	@ResponseQrCode
	@PostMapping("/json/mono")
	public Mono<Map<String, String>> jsonMono(@RequestBody JsonNode node) {
		return Mono.just(JsonMapperUtils.convertValueMap(node, String.class, String.class));
	}

}
