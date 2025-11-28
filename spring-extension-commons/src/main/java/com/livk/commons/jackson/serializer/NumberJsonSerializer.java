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

package com.livk.commons.jackson.serializer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Set;

/**
 * NumberJsonFormat序列化处理
 *
 * @author livk
 * @see NumberJsonFormat
 */
@AllArgsConstructor
@NoArgsConstructor
class NumberJsonSerializer extends ValueSerializer<Number> {

	private static final Set<Class<?>> SUPPORT_PRIMITIVE_CLASS = Set.of(byte.class, short.class, int.class, long.class,
			float.class, double.class);

	private String format;

	@Override
	public void serialize(Number value, JsonGenerator gen, SerializationContext context) throws JacksonException {
		gen.writeString(new DecimalFormat(format).format(value));
	}

	@Override
	public ValueSerializer<?> createContextual(SerializationContext context, BeanProperty property) {
		Class<?> rawClass = property.getType().getRawClass();
		NumberJsonFormat jsonFormat = Optional.ofNullable(property.getAnnotation((NumberJsonFormat.class)))
			.orElse(property.getContextAnnotation(NumberJsonFormat.class));
		if (Number.class.isAssignableFrom(rawClass) || this.simpleTypeSupport(rawClass)) {
			return new NumberJsonSerializer(jsonFormat.pattern());
		}
		return context.findContentValueSerializer(property.getType(), property);
	}

	private boolean simpleTypeSupport(Class<?> rawClass) {
		return rawClass.isPrimitive() && SUPPORT_PRIMITIVE_CLASS.contains(rawClass);
	}

}
