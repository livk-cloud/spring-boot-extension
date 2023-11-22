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
 * 自定义{@link Binder}
 * <p>
 * 用于添加{@link Converter}
 * <p>
 * 便于解析Environment成各种类型的数据
 *
 * @author livk
 * @see Binder
 */
public class SpringEnvBinder {

	private final Environment environment;

	private final ConfigurableConversionService conversionService;

	/**
	 * 构造方法
	 * @param environment the environment
	 * @param conversionService the conversion service
	 */
	public SpringEnvBinder(Environment environment, ConfigurableConversionService conversionService) {
		this.environment = environment;
		this.conversionService = conversionService;
	}

	/**
	 * 构造方法
	 * @param environment the environment
	 */
	public SpringEnvBinder(Environment environment) {
		this(environment, new DefaultConversionService());
	}

	/**
	 * 添加Converter
	 * @param converter the converter
	 * @return the spring env binder
	 */
	public SpringEnvBinder addConverter(Converter<?, ?> converter) {
		conversionService.addConverter(converter);
		return this;
	}

	/**
	 * 构建Binder
	 * @return the binder
	 */
	public Binder binder() {
		Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
		PropertySourcesPlaceholdersResolver placeholdersResolver = new PropertySourcesPlaceholdersResolver(environment);
		return new Binder(sources, placeholdersResolver, conversionService);
	}

	/**
	 * 构建BindResult,返回类型与ResolvableType相关
	 * @param <T> 泛型
	 * @param name 要绑定的配置属性名称
	 * @param type 类型相关
	 * @return bind result
	 */
	public <T> BindResult<T> of(String name, ResolvableType type) {
		return bind(name, Bindable.of(type));
	}

	/**
	 * 构建BindResult,返回数据为List
	 * @param <E> 泛型
	 * @param name 要绑定的配置属性名称
	 * @param type 泛型类型
	 * @return bind result
	 */
	public <E> BindResult<List<E>> listOf(String name, Class<E> type) {
		return bind(name, Bindable.listOf(type));
	}

	/**
	 * 构建BindResult,返回数据为Set
	 * @param <E> 泛型
	 * @param name 要绑定的配置属性名称
	 * @param type 泛型类型
	 * @return bind result
	 */
	public <E> BindResult<Set<E>> setOf(String name, Class<E> type) {
		return bind(name, Bindable.setOf(type));
	}

	/**
	 * 构建BindResult,返回数据为Map
	 * @param <K> Key泛型
	 * @param <V> Value泛型
	 * @param name 要绑定的配置属性名称
	 * @param kType Key类型
	 * @param vType Value类型
	 * @return bind result
	 */
	public <K, V> BindResult<Map<K, V>> mapOf(String name, Class<K> kType, Class<V> vType) {
		return bind(name, Bindable.mapOf(kType, vType));
	}

	/**
	 * 构建BindResult,返回数据为Properties
	 * @param name 要绑定的配置属性名称
	 * @return bind result
	 */
	public BindResult<Properties> propertiesOf(String name) {
		return bind(name, Properties.class);
	}

	/**
	 * 构建BindResult
	 * @param <T> 泛型
	 * @param name 要绑定的配置属性名称
	 * @param bindable bindable
	 * @return bind result
	 */
	public <T> BindResult<T> bind(String name, Bindable<T> bindable) {
		return binder().bind(name, bindable);
	}

	/**
	 * 构建BindResult
	 * @param <T> 泛型
	 * @param name 要绑定的配置属性名称
	 * @param type 返回数据类型
	 * @return bind result
	 */
	public <T> BindResult<T> bind(String name, Class<T> type) {
		return binder().bind(name, type);
	}

}
