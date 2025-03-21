/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.crypto.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.CryptoEncrypt;
import com.livk.crypto.fotmat.CryptoFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Optional;

/**
 * @author livk
 */
@NoArgsConstructor
@AllArgsConstructor
public class CryptoJsonSerializer extends JsonSerializer<Object> implements ContextualSerializer {

	private CryptoFormatter<Object> formatter;

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String text = formatter.format(value);
		CryptoType type = formatter.type();
		gen.writeString(type.wrapper(text));
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		JavaType javaType = property.getType();
		CryptoEncrypt encrypt = Optional.ofNullable(property.getAnnotation((CryptoEncrypt.class)))
			.orElse(property.getContextAnnotation(CryptoEncrypt.class));
		CryptoFormatter<?> printer = getCryptoFormatter(javaType.getRawClass(), encrypt.value());
		if (printer != null) {
			return new CryptoJsonSerializer((CryptoFormatter<Object>) printer);
		}
		return prov.findValueSerializer(javaType, property);
	}

	private CryptoFormatter<?> getCryptoFormatter(Class<?> rawClass, CryptoType type) {
		for (CryptoFormatter<?> formatter : CryptoFormatter.fromContext().get(rawClass)) {
			if (formatter.type().equals(type)) {
				return formatter;
			}
		}
		return null;
	}

}
