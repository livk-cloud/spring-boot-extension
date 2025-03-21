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

package com.livk.crypto.fotmat;

import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.CryptoDecrypt;
import com.livk.crypto.exception.FormatterNotFountException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author livk
 */
public class SpringAnnotationFormatterFactory implements AnnotationFormatterFactory<CryptoDecrypt> {

	private final Map<Class<?>, List<CryptoFormatter<?>>> map;

	public SpringAnnotationFormatterFactory(ObjectProvider<CryptoFormatter<?>> cryptoFormatters) {
		map = cryptoFormatters.orderedStream().collect(Collectors.groupingBy(CryptoFormatter::supportClass));
	}

	@NonNull
	@Override
	public Set<Class<?>> getFieldTypes() {
		return map.keySet();
	}

	@NonNull
	@Override
	public Printer<?> getPrinter(@NonNull CryptoDecrypt annotation, @NonNull Class<?> fieldType) {
		throw new UnsupportedOperationException();
	}

	@NonNull
	@Override
	public Parser<?> getParser(@NonNull CryptoDecrypt annotation, @NonNull Class<?> fieldType) {
		return (text, locale) -> {
			CryptoType type = CryptoType.match(text);
			for (CryptoFormatter<?> parser : map.get(fieldType)) {
				if (type.equals(parser.type())) {
					return parser.parse(type.unwrap(text), locale);
				}
			}
			throw new FormatterNotFountException("fieldType:" + fieldType + " Parser NotFount!");
		};
	}

}
