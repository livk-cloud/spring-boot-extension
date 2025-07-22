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

package com.livk.proto.pulsar.config;

import com.livk.proto.User;
import com.livk.proto.pulsar.converter.UserProtobufSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.pulsar.core.DefaultSchemaResolver;
import org.springframework.pulsar.core.SchemaResolver;

/**
 * @author livk
 */
public class PulsarConfig {

	public static final String TOPIC_NAME = "pulsar-protobuf";

	/**
	 * <a href=
	 * "https://docs.spring.io/spring-pulsar/docs/current-SNAPSHOT/reference/html/#_custom_schema_mapping_2">Spring
	 * Pulsar</a>
	 */
	@Bean
	public SchemaResolver.SchemaResolverCustomizer<DefaultSchemaResolver> schemaResolverCustomizer() {
		return schemaResolver -> schemaResolver.addCustomSchemaMapping(User.class, new UserProtobufSchema());
	}

}
