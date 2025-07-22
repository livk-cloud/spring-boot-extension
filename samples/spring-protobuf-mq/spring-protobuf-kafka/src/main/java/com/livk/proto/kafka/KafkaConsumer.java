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

package com.livk.proto.kafka;

import com.livk.proto.ConsumerCheck;
import com.livk.proto.User;
import com.livk.proto.kafka.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
public class KafkaConsumer {

	@KafkaListener(id = "consumer", topics = KafkaConfig.TOPIC_NAME)
	public void consumer(ConsumerRecord<String, User> record) {
		log.info("topic[{}],offset[{}],partition[{}],key[{}],val[{}]", record.topic(), record.offset(),
				record.partition(), record.key(), record.value());
		ConsumerCheck.success();
	}

}
