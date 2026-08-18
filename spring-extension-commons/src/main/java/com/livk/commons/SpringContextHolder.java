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

package com.livk.commons;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.commons.util.GenericWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.env.Environment;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Static utility for accessing the Spring application context.
 *
 * @author livk
 */
@SpringAutoService
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component(SpringContextHolder.BEAN_NAME)
public class SpringContextHolder implements BeanFactoryAware, ApplicationContextAware, DisposableBean {

	/**
	 * SpringContextHolder bean name.
	 */
	public static final String BEAN_NAME = "com.livk.commons.SpringContextHolder";

	private static final SpringIoC IOC = new SpringIoC();

	/**
	 * Publishes a Spring application event.
	 * @param <E> the event type
	 * @param event 事件
	 */
	public static <E extends ApplicationEvent> void publishEvent(E event) {
		IOC.unwrap().publishEvent(event);
	}

	/**
	 * Gets a bean by name.
	 * @param <T> 泛型
	 * @param name beanName
	 * @return spring bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) IOC.getBeanFactory().getBean(name);
	}

	/**
	 * Gets a bean by type.
	 * @param <T> 泛型
	 * @param typeClass 类型
	 * @return spring bean
	 */
	public static <T> T getBean(Class<T> typeClass) {
		return IOC.getBeanFactory().getBean(typeClass);
	}

	/**
	 * Gets a bean by name and type.
	 * @param <T> 泛型
	 * @param name beanName
	 * @param typeClass 类型
	 * @return spring bean
	 */
	public static <T> T getBean(String name, Class<T> typeClass) {
		return IOC.getBeanFactory().getBean(name, typeClass);
	}

	/**
	 * Gets an ObjectProvider by type for lazy loading.
	 * @param <T> 泛型
	 * @param typeClass 类型
	 * @return spring bean provider
	 */
	public static <T> ObjectProvider<T> getBeanProvider(Class<T> typeClass) {
		return IOC.getBeanFactory().getBeanProvider(typeClass);
	}

	/**
	 * Gets an ObjectProvider by ResolvableType for lazy loading.
	 * @param <T> 泛型
	 * @param resolvableType 类型相关
	 * @return spring bean provider
	 */
	public static <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType) {
		return IOC.getBeanFactory().getBeanProvider(resolvableType);
	}

	/**
	 * Gets beans of the specified type as a map.
	 * @param <T> 泛型
	 * @param typeClass 类型
	 * @return spring bean map
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> typeClass) {
		return IOC.getBeanFactory().getBeansOfType(typeClass);
	}

	/**
	 * Gets an environment property value by key.
	 * @param key key
	 * @return string
	 */
	public static String getProperty(String key) {
		return getProperty(key, String.class);
	}

	/**
	 * Gets an environment property value by key and converts to the required type.
	 * @param <T> 泛型
	 * @param key key
	 * @param requiredType 返回类型
	 * @return value
	 */
	public static <T> T getProperty(String key, Class<T> requiredType) {
		return IOC.unwrap().getEnvironment().getProperty(key, requiredType);
	}

	/**
	 * Gets a property value with a default fallback.
	 * @param <T> 泛型
	 * @param key key
	 * @param requiredType 返回类型
	 * @param defaultValue 默认的返回数据
	 * @return value
	 */
	public static <T> T getProperty(String key, Class<T> requiredType, T defaultValue) {
		return IOC.unwrap().getEnvironment().getProperty(key, requiredType, defaultValue);
	}

	/**
	 * Resolves placeholders in the given text.
	 * @param text string
	 * @return value
	 */
	public static String resolvePlaceholders(String text) {
		return IOC.unwrap().getEnvironment().resolvePlaceholders(text);
	}

	/**
	 * Creates a Binder with additional converters.
	 * @param converters 添加转换器
	 * @return binder
	 */
	public static Binder binder(Converter<?, ?>... converters) {
		Environment environment = IOC.unwrap().getEnvironment();
		Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
		PropertySourcesPlaceholdersResolver placeholdersResolver = new PropertySourcesPlaceholdersResolver(environment);
		GenericConversionService service = new GenericConversionService();
		Arrays.stream(converters).forEach(service::addConverter);
		return new Binder(sources, placeholdersResolver, service);
	}

	/**
	 * Registers a bean instance in the application context.
	 * @param <T> bean类型
	 * @param bean bean实例
	 * @param beanName beanName可为空，为空会自动生成
	 */
	public static <T> void registerBean(T bean, String beanName) {
		ResolvableType resolvableType = ResolvableType.forInstance(bean);
		BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(resolvableType, () -> bean);
		registerBean(definitionBuilder.getBeanDefinition(), beanName);
	}

	/**
	 * Registers a bean definition in the application context.
	 * @param beanDefinition bean定义
	 * @param beanName beanName
	 */
	public static void registerBean(BeanDefinition beanDefinition, String beanName) {
		if (IOC.getBeanFactory() instanceof DefaultListableBeanFactory defaultBeanFactory) {
			registerBean(defaultBeanFactory, beanDefinition, beanName);
		}
		else if (IOC.unwrap() instanceof GenericApplicationContext context) {
			registerBean(context, beanDefinition, beanName);
		}
		else {
			throw new BeanCreationNotAllowedException(beanName,
					"The current BeanFactory does not support dynamic bean registration");
		}
	}

	private static void registerBean(BeanDefinitionRegistry registry, BeanDefinition beanDefinition, String beanName) {
		beanName = StringUtils.hasText(beanName) ? beanName
				: BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);
		registry.registerBeanDefinition(beanName, beanDefinition);
	}

	public static ApplicationContext fetch() {
		return IOC.unwrap();
	}

	@Override
	public void setBeanFactory(@Nullable BeanFactory beanFactory) throws BeansException {
		SpringContextHolder.IOC.beanFactory(beanFactory);
	}

	@Override
	public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.IOC.applicationContext(applicationContext);
	}

	@Override
	public void destroy() {
		SpringContextHolder.IOC.clear();
	}

	private static final class SpringIoC implements GenericWrapper<ApplicationContext> {

		private volatile ApplicationContext context;

		private volatile BeanFactory factory;

		private ListableBeanFactory getBeanFactory() {
			BeanFactory currentFactory = this.factory;
			ApplicationContext currentContext = this.context;
			if (currentFactory == null && currentContext == null) {
				throw new IllegalStateException("SpringContextHolder is not initialized yet");
			}
			return (currentFactory instanceof ListableBeanFactory beanFactory) ? beanFactory : currentContext;
		}

		@Override
		public ApplicationContext unwrap() {
			BeanFactory currentFactory = this.factory;
			ApplicationContext currentContext = this.context;
			ApplicationContext applicationContext = (currentFactory instanceof ApplicationContext contextFactory)
					? contextFactory : currentContext;
			if (applicationContext == null) {
				throw new IllegalStateException("SpringContextHolder is not initialized yet");
			}
			return applicationContext;
		}

		public void beanFactory(BeanFactory beanFactory) {
			this.factory = beanFactory;
		}

		public void applicationContext(ApplicationContext applicationContext) {
			this.context = applicationContext;
		}

		public void clear() {
			this.factory = null;
			this.context = null;
		}

	}

}
