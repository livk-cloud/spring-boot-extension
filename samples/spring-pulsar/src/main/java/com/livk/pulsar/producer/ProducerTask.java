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

package com.livk.pulsar.producer;

import com.livk.commons.util.SnowflakeIdGenerator;
import com.livk.pulsar.producer.entity.PulsarMessage;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.Schema;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class ProducerTask {

	final SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 2);

	private final PulsarTemplate<String> pulsarTemplate;

	@Scheduled(cron = "0/5 * * * * ?")
	public void send() {
		PulsarMessage<String> message = new PulsarMessage<>();
		message.setMsgId(UUID.randomUUID().toString());
		message.setSendTime(LocalDateTime.now());
		message.setData(String.valueOf(generator.nextId()));

		pulsarTemplate.newMessage(message.toJson())
			.withSchema(Schema.STRING)
			.withMessageCustomizer(builder -> builder.key(UUID.randomUUID().toString().substring(0, 5)))
			.sendAsync()
			.handle((messageId, throwable) -> throwable == null)
			.join();
	}

}
