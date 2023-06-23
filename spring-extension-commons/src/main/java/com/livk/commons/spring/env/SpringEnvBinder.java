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

package com.livk.commons.spring.env;

import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * The type Spring env binder.
 *
 * @author livk
 */
public class SpringEnvBinder {

	private final Environment environment;

	private final ConfigurableConversionService conversionService;

	/**
	 * Instantiates a new Spring env binder.
	 *
	 * @param environment       the environment
	 * @param conversionService the conversion service
	 */
	public SpringEnvBinder(Environment environment, ConfigurableConversionService conversionService) {
		this.environment = environment;
		this.conversionService = conversionService;
	}

	/**
	 * Instantiates a new Spring env binder.
	 *
	 * @param environment the environment
	 */
	public SpringEnvBinder(Environment environment) {
		this(environment, new DefaultConversionService());
	}

	/**
	 * Add converter spring env binder.
	 *
	 * @param converter the converter
	 * @return the spring env binder
	 */
	public SpringEnvBinder addConverter(Converter<?, ?> converter) {
		conversionService.addConverter(converter);
		return this;
	}

	/**
	 * Binder binder.
	 *
	 * @return the binder
	 */
	public Binder binder() {
		Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
		PropertySourcesPlaceholdersResolver placeholdersResolver = new PropertySourcesPlaceholdersResolver(environment);
		return new Binder(sources, placeholdersResolver, conversionService,
			null, null, null);
	}

	/**
	 * Of bind result.
	 *
	 * @param <T>  the type parameter
	 * @param name the name
	 * @param type the type
	 * @return the bind result
	 */
	public <T> BindResult<T> of(String name, ResolvableType type) {
		return bind(name, Bindable.of(type));
	}

	/**
	 * List of bind result.
	 *
	 * @param <E>  the type parameter
	 * @param name the name
	 * @param type the type
	 * @return the bind result
	 */
	public <E> BindResult<List<E>> listOf(String name, Class<E> type) {
		return bind(name, Bindable.listOf(type));
	}

	/**
	 * Sets of.
	 *
	 * @param <E>  the type parameter
	 * @param name the name
	 * @param type the type
	 * @return the of
	 */
	public <E> BindResult<Set<E>> setOf(String name, Class<E> type) {
		return bind(name, Bindable.setOf(type));
	}

	/**
	 * Map of bind result.
	 *
	 * @param <K>   the type parameter
	 * @param <V>   the type parameter
	 * @param name  the name
	 * @param kType the k type
	 * @param vType the v type
	 * @return the bind result
	 */
	public <K, V> BindResult<Map<K, V>> mapOf(String name, Class<K> kType, Class<V> vType) {
		return bind(name, Bindable.mapOf(kType, vType));
	}

	/**
	 * Properties of bind result.
	 *
	 * @param name the name
	 * @return the bind result
	 */
	public BindResult<Properties> propertiesOf(String name) {
		return bind(name, Properties.class);
	}

	/**
	 * Bind bind result.
	 *
	 * @param <T>      the type parameter
	 * @param name     the key prefix
	 * @param bindable the bindable
	 * @return the bind result
	 */
	public <T> BindResult<T> bind(String name, Bindable<T> bindable) {
		return binder().bind(name, bindable);
	}

	/**
	 * Bind bind result.
	 *
	 * @param <T>  the type parameter
	 * @param name the key prefix
	 * @param type the bindable
	 * @return the bind result
	 */
	public <T> BindResult<T> bind(String name, Class<T> type) {
		return binder().bind(name, type);
	}
}
