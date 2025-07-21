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

package com.livk.proto.kafka.config;

import com.livk.proto.kafka.converter.ProtobufDeserializer;
import com.livk.proto.kafka.converter.ProtobufSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author livk
 */
@Configuration
public class KafkaConfig {

	public static final String TOPIC_NAME = "kafka-protobuf";

	@Bean
	public NewTopic myTopic() {
		return new NewTopic(TOPIC_NAME, 1, (short) 1);
	}

	@Bean
	public DefaultKafkaConsumerFactoryCustomizer userProtobufConsumerFactoryCustomizer() {
		return consumerFactory -> consumerFactory.setValueDeserializerSupplier(ProtobufDeserializer::new);
	}

	@Bean
	public DefaultKafkaProducerFactoryCustomizer userProtobufProducerFactoryCustomizer() {
		return producerFactory -> producerFactory.setValueSerializerSupplier(ProtobufSerializer::new);
	}

}
