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
import lombok.RequiredArgsConstructor;

/**
 * The type Abstract jackson operations.
 *
 * @author livk
 */
@RequiredArgsConstructor
public abstract class AbstractJacksonOps implements JacksonOps {

	private final TypeFactory typeFactory;

	/**
	 * Instantiates a new Abstract jackson operations.
	 */
	public AbstractJacksonOps() {
		this(TypeFactoryUtils.instance());
	}

	/**
	 * Read value t.
	 *
	 * @param <T>  the type parameter
	 * @param obj  the obj
	 * @param type the type
	 * @return the t
	 */
	public final <T> T readValue(Object obj, Class<T> type) {
		return readValue(obj, typeFactory.constructType(type));
	}

	/**
	 * Read value t.
	 *
	 * @param <T>           the type parameter
	 * @param obj           the obj
	 * @param typeReference the type reference
	 * @return the t
	 */
	public final <T> T readValue(Object obj, TypeReference<T> typeReference) {
		return readValue(obj, typeFactory.constructType(typeReference));
	}

	/**
	 * Convert object.
	 *
	 * @param <T>       the type parameter
	 * @param fromValue the  value
	 * @param type      the type
	 * @return the object
	 */
	public final <T> T convertValue(Object fromValue, Class<T> type) {
		return convertValue(fromValue, typeFactory.constructType(type));
	}

	/**
	 * Convert value t.
	 *
	 * @param <T>           the type parameter
	 * @param fromValue     the value
	 * @param typeReference the type reference
	 * @return the t
	 */
	public final <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
		return convertValue(fromValue, typeFactory.constructType(typeReference));
	}
}
