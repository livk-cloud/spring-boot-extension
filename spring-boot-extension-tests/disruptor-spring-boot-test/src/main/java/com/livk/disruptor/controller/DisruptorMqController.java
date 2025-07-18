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

package com.livk.disruptor.controller;

import com.livk.disruptor.service.DisruptorMqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("msg")
@RequiredArgsConstructor
public class DisruptorMqController {

	private final DisruptorMqService disruptorMqService;

	@PostMapping
	public HttpEntity<Void> send() {
		disruptorMqService.send("消息到了，Hello world!");
		return ResponseEntity.ok().build();
	}

	@PostMapping("batch")
	public HttpEntity<Void> sendBatch() {
		List<String> messages = IntStream.range(1, 10)
			.mapToObj(value -> "消息到了," + value + "，Hello world!----producer")
			.toList();
		disruptorMqService.batch(messages);
		return ResponseEntity.ok().build();
	}

}
