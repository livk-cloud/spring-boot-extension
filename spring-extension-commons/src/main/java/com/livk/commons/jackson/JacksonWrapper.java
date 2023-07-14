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

package com.livk.commons.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.commons.beans.GenericWrapper;
import com.livk.commons.spring.context.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * The type Jackson wrapper.
 *
 * @author livk
 */
@Slf4j
@Deprecated(forRemoval = true)
@RequiredArgsConstructor
public class JacksonWrapper implements GenericWrapper<ObjectMapper> {

	/**
	 * The constant BEAN_NAME.
	 */
	public static final String BEAN_NAME = "com.livk.commons.jackson.JacksonWrapper";

	private final ObjectMapper mapper;

	/**
	 * Unwrap of context object mapper.
	 *
	 * @return the object mapper
	 */
	public static ObjectMapper unwrapOfContext() {
		GenericWrapper<ObjectMapper> wrapper = null;
		try {
			if (SpringContextHolder.getApplicationContext().containsBean(JacksonWrapper.BEAN_NAME)) {
				wrapper = SpringContextHolder.getBean(JacksonWrapper.BEAN_NAME, JacksonWrapper.class);
			} else {
				throw new NoSuchBeanDefinitionException(JacksonWrapper.BEAN_NAME);
			}
		} catch (Exception e) {
			log.debug("Building 'ObjectMapper'");
		}
		return wrapper == null ? JsonMapper.builder().build() : wrapper.unwrap();
	}

	@Override
	public ObjectMapper unwrap() {
		return mapper;
	}
}
