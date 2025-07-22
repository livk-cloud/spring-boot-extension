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

package com.livk.order.controller;

import com.livk.order.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

	private final AmqpTemplate rabbitTemplate;

	/**
	 * 模拟提交订单
	 * @return java.lang.Object
	 * @author nxq
	 */
	@GetMapping
	public HttpEntity<Map<String, String>> submit() {
		String orderId = UUID.randomUUID().toString();
		log.info("submit order {}", orderId);
		this.rabbitTemplate.convertAndSend(RabbitConfig.orderExchange, RabbitConfig.routingKeyOrder, orderId);
		return ResponseEntity.ok(Map.of("orderId", orderId));
	}

}
