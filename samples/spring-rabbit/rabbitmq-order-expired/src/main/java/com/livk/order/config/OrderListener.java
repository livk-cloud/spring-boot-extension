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

package com.livk.order.config;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author livk
 */
@Slf4j
@Component
public class OrderListener {

	@RabbitListener(queues = RabbitConfig.dealQueueOrder)
	public void processFail(String order, Message message, Channel channel) throws IOException {
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		log.error("【订单号】 - [{}]被取消", order);
	}

	@RabbitListener(queues = RabbitConfig.orderQueue)
	public void processSuc(String order, Message message, Channel channel) throws IOException, InterruptedException {
		log.info("【订单号】 - [{}]", order);
		TimeUnit.SECONDS.sleep(1);
		// 是否支付
		int i = ThreadLocalRandom.current().nextInt(1, 100);
		if (i == 6) {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			log.info("支付成功！");
		}
		else {
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			log.warn("缺少支付！");
		}
	}

}
