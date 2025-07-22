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

package com.livk.pulsar.producer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.pulsar.producer.entity.PulsarMessage;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Schema;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author livk
 */
@RestController
@RequestMapping("producer")
@RequiredArgsConstructor
public class MessageController {

	private final PulsarTemplate<String> pulsarTemplate;

	@PostMapping
	public HttpEntity<String> send(@RequestBody JsonNode jsonNode) throws Exception {
		PulsarMessage<JsonNode> message = new PulsarMessage<>();
		message.setMsgId(UUID.randomUUID().toString());
		message.setSendTime(LocalDateTime.now());
		message.setData(jsonNode);

		MessageId messageId = pulsarTemplate.sendAsync(message.toJson(), Schema.STRING).get();
		return ResponseEntity.ok(JsonMapperUtils.writeValueAsString(messageId));
	}

}
