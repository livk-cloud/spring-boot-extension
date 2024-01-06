/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.jackson.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.livk.commons.jackson.util.TypeFactoryUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Jackson相关操作抽象类
 *
 * @author livk
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractJacksonOps implements JacksonOps {

	private final TypeFactory typeFactory;

	/**
	 * 无参构造
	 */
	protected AbstractJacksonOps() {
		this(TypeFactoryUtils.instance());
	}

	public final <T> T readValue(Object obj, Class<T> type) {
		return readValue(obj, typeFactory.constructType(type));
	}

	public final <T> T readValue(Object obj, TypeReference<T> typeReference) {
		return readValue(obj, typeFactory.constructType(typeReference));
	}

	public final <T> T convertValue(Object fromValue, Class<T> type) {
		return convertValue(fromValue, typeFactory.constructType(type));
	}

	public final <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
		return convertValue(fromValue, typeFactory.constructType(typeReference));
	}

}
