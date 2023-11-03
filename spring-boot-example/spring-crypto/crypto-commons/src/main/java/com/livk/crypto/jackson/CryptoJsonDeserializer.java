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

package com.livk.crypto.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.livk.crypto.CryptoType;
import com.livk.crypto.fotmat.CryptoFormatter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author livk
 */
@NoArgsConstructor
public class CryptoJsonDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

	private JavaType javaType;

	private JsonDeserializer<Object> defaultJsonDeserializer;

	@Override
	public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
		String text = context.readTree(p).textValue();
		CryptoType type = CryptoType.match(text);
		CryptoFormatter<?> parser = getCryptoFormatter(type);
		if (parser == null) {
			return defaultJsonDeserializer.deserialize(p, context);
		}
		try {
			return parser.parse(type.unwrap(text));
		} catch (ParseException e) {
			throw new JsonParseException(p, e.getMessage());
		}
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) throws JsonMappingException {
		javaType = property.getType();
		defaultJsonDeserializer = context.findContextualValueDeserializer(javaType, property);
		return this;
	}

	private CryptoFormatter<?> getCryptoFormatter(CryptoType type) {
		for (CryptoFormatter<?> formatter : CryptoFormatter.fromContext().get(javaType.getRawClass())) {
			if (formatter.type().equals(type)) {
				return formatter;
			}
		}
		return null;
	}
}
