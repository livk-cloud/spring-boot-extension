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

package com.livk.commons.jackson;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

/**
 * @author livk
 */
@SpringAutoService
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class JacksonConfiguration {

	/**
	 * 自定义JacksonBuilder的配置
	 * @return the jackson 2 object mapper builder customizer
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer javaTimeCustomizer() {
		return builder -> builder.locale(Locale.CHINA).modules(new JavaTimeModule());
	}

}
