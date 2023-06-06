/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.rocket.consumer.listener;

import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.rocket.constant.RocketConstant;
import com.livk.rocket.dto.RocketDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author laokou
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "livk-consumer-group-2", topic = RocketConstant.LIVK_EXT_MESSAGE_TOPIC)
public class TestExtMessageListener implements RocketMQListener<RocketDTO> {

	@Override
	public void onMessage(RocketDTO dto) {
		if (new Random(20).nextInt() % 20 == 0) {
			throw new RuntimeException("ext 消息重试");
		}
		log.info("ext rocketMQ receive message:{}", JsonMapperUtils.writeValueAsString(dto));
	}
}
