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

package com.livk.core.mapstruct.converter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * <p>
 * ConverterPair
 * </p>
 *
 * @author livk
 */
@Getter
@ToString
@EqualsAndHashCode
public class ConverterPair {

	private final Class<?> sourceType;

	private final Class<?> targetType;

	private ConverterPair(Class<?> sourceType, Class<?> targetType) {
		Assert.notNull(sourceType, "Source type must not be null");
		Assert.notNull(targetType, "Target type must not be null");
		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	/**
	 * Of converter pair.
	 * @param sourceType the source type
	 * @param targetType the target type
	 * @return the converter pair
	 */
	public static ConverterPair of(Class<?> sourceType, Class<?> targetType) {
		return new ConverterPair(sourceType, targetType);
	}

}
