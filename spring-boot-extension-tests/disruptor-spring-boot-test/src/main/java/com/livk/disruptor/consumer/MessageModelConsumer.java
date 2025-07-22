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

package com.livk.disruptor.consumer;

import com.livk.context.disruptor.support.DisruptorEventConsumer;
import com.livk.disruptor.event.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageModelConsumer implements DisruptorEventConsumer<MessageModel> {

	private final ApplicationContext applicationContext;

	@Override
	public void onEvent(MessageModel wrapper, long sequence, boolean endOfBatch) {
		log.info("消费者消费的信息是：{} :{} :{} id:{}", wrapper, sequence, endOfBatch, applicationContext.getId());
	}

}
