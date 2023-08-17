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

package com.livk.crypto.fotmat;

import com.livk.commons.spring.context.SpringContextHolder;
import com.livk.crypto.CryptoType;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The interface Spring context parser.
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface CryptoFormatter<T> extends Formatter<T> {

	/**
	 * From context object provider.
	 *
	 * @return the object provider
	 */
	static Map<Class<?>, List<CryptoFormatter<?>>> fromContext() {
		return fromContext(SpringContextHolder.getApplicationContext());
	}

	static Map<Class<?>, List<CryptoFormatter<?>>> fromContext(BeanFactory beanFactory) {
		ResolvableType resolvableType = ResolvableType.forClass(CryptoFormatter.class);
		return beanFactory.<CryptoFormatter<?>>getBeanProvider(resolvableType)
			.orderedStream()
			.collect(Collectors.groupingBy(CryptoFormatter::supportClass));
	}


	@Override
	default T parse(String text, Locale locale) throws ParseException {
		return parse(text);
	}

	@Override
	default String print(T object, Locale locale) {
		return format(object);
	}

	/**
	 * Support class.
	 *
	 * @return the class
	 */
	default Class<?> supportClass() {
		return GenericTypeResolver.resolveTypeArgument(this.getClass(), CryptoFormatter.class);
	}

	String format(T value);

	T parse(String text) throws ParseException;

	/**
	 * Type crypto type.
	 *
	 * @return the crypto type
	 */
	CryptoType type();
}
