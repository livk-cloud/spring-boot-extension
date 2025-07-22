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

package com.livk.proto.amqp;

import com.livk.proto.ConsumerCheck;
import com.livk.proto.User;
import com.livk.proto.amqp.config.AmqpConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
public class AmqpConsumer {

	@RabbitListener(queuesToDeclare = @Queue(AmqpConfig.TOPIC_NAME))
	public void consumer(@Payload User user) {
		log.info(" : {}", user);
		ConsumerCheck.success();
	}

}
