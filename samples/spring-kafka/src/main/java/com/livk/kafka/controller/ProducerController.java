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

package com.livk.kafka.controller;

import com.livk.kafka.KafkaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("kafka")
@RequiredArgsConstructor
public class ProducerController {

	private final KafkaTemplate<Object, Object> kafkaTemplate;

	@GetMapping("send")
	public void producer() {
		kafkaTemplate.send(KafkaConstant.TOPIC, UUID.randomUUID().toString());
		// 异步获取结果
		kafkaTemplate.send(KafkaConstant.TOPIC, UUID.randomUUID().toString()).whenComplete((result, throwable) -> {
			if (throwable != null) {
				log.error("ex:{}", throwable.getMessage());
			}
			else {
				log.info("result:{}", result);
			}
		});
		// 同步获取结果
		CompletableFuture<SendResult<Object, Object>> future = kafkaTemplate.send(KafkaConstant.TOPIC,
				UUID.randomUUID().toString());
		try {
			SendResult<Object, Object> result = future.get();
			log.info("result:{}", result);
		}
		catch (Exception e) {
			log.error("ex:{}", e.getMessage());
		}
	}

}
