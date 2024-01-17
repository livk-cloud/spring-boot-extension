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

package com.livk.disruptor.service.impl;

import com.livk.core.disruptor.DisruptorEventProducer;
import com.livk.core.disruptor.support.SpringDisruptor;
import com.livk.disruptor.event.MessageModel;
import com.livk.disruptor.service.DisruptorMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author livk
 */
@Slf4j
@Service
public class DisruptorMqServiceImpl implements DisruptorMqService {

	private final DisruptorEventProducer<MessageModel> producer;

	public DisruptorMqServiceImpl(SpringDisruptor<MessageModel> disruptor) {
		producer = new DisruptorEventProducer<>(disruptor);
	}

	@Override
	public void send(String message) {
		log.info("record the message: {}", message);
		producer.send(toMessageModel(message));
	}

	@Override
	public void batch(List<String> messages) {
		List<MessageModel> messageModels = messages.stream().map(this::toMessageModel).toList();
		producer.sendBatch(messageModels);
	}

	private MessageModel toMessageModel(String message) {
		return MessageModel.builder().message(message).build();
	}

}
