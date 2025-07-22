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

package com.livk.event.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.event.context.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SentController {

	private final SseEmitterRepository<String> sseEmitterRepository;

	@GetMapping("/subscribe/{id}")
	public HttpEntity<SseEmitter> subscribe(@PathVariable String id) {
		SseEmitter sseEmitter = sseEmitterRepository.get(id);
		if (sseEmitter == null) {
			sseEmitter = new SseEmitter(3600_000L);
			sseEmitterRepository.put(id, sseEmitter);
			sseEmitter.onTimeout(() -> sseEmitterRepository.remove(id));
			sseEmitter.onCompletion(() -> log.warn("推送结束！"));
		}
		return ResponseEntity.ok(sseEmitter);
	}

	@PostMapping("/push/{id}")
	public HttpEntity<Boolean> push(@PathVariable String id, @RequestBody JsonNode content) throws IOException {
		log.info("{}", content);
		SseEmitter sseEmitter = sseEmitterRepository.get(id);
		if (sseEmitter == null) {
			return ResponseEntity.ok(false);
		}
		sseEmitter.send(SseEmitter.event().data(content).id(id));
		return ResponseEntity.ok(true);
	}

	@PostMapping("/push/data/{id}")
	public HttpEntity<Boolean> pushData(@PathVariable String id) throws IOException {
		SseEmitter sseEmitter = sseEmitterRepository.get(id);
		if (sseEmitter == null) {
			return ResponseEntity.ok(false);
		}
		for (int i = 0; i < 10; i++) {
			sseEmitter.send(SseEmitter.event().data(i + ":::::" + UUID.randomUUID()).id(id));
		}
		sseEmitter.complete();
		sseEmitterRepository.remove(id);
		return ResponseEntity.ok(true);
	}

	@DeleteMapping("/over/{id}")
	public HttpEntity<Boolean> over(@PathVariable String id) {
		SseEmitter sseEmitter = sseEmitterRepository.get(id);
		if (sseEmitter == null) {
			return ResponseEntity.ok(false);
		}
		sseEmitter.complete();
		sseEmitterRepository.remove(id);
		return ResponseEntity.ok(true);
	}

}
