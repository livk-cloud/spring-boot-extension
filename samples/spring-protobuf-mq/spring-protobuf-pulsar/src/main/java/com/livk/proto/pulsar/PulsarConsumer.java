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

package com.livk.proto.pulsar;

import com.livk.proto.ConsumerCheck;
import com.livk.proto.User;
import com.livk.proto.pulsar.config.PulsarConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
public class PulsarConsumer {

	@PulsarListener(id = "pulsar", subscriptionName = "consumer", topics = PulsarConfig.TOPIC_NAME)
	public void receive(Message<User> message) {
		String key = message.getKey();
		User data = message.getValue();
		String topic = message.getTopicName();
		log.info("topic:{} key:{} data:{}", topic, key, data);
		ConsumerCheck.success();
	}

}
