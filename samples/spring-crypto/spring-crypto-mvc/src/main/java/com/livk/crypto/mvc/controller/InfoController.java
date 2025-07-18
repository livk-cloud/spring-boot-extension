/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.crypto.mvc.controller;

import com.livk.crypto.annotation.CryptoDecrypt;
import com.livk.crypto.mvc.entity.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("/info")
public class InfoController {

	@PostMapping
	public Map<String, Info> info(@RequestBody Info info) {
		log.info("RequestBody:{}", info);
		return Map.of("body", info);
	}

	@GetMapping
	public Map<String, Info> info(@RequestHeader("id") @CryptoDecrypt Long headerId,
			@RequestParam("id") @CryptoDecrypt Long paramId) {
		log.info("RequestHeader:{}", headerId);
		log.info("RequestParam:{}", paramId);
		return Map.of("id", new Info(headerId, paramId));
	}

}
