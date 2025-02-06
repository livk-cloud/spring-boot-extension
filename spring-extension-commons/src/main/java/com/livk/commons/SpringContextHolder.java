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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * spring context静态操作工具
 * </p>
 *
 * @author livk
 */
@SpringAutoService
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component(SpringContextHolder.BEAN_NAME)
public class SpringContextHolder implements BeanFactoryAware, ApplicationContextAware, DisposableBean {

	/**
	 * SpringContextHolder bean name
	 */
	public static final String BEAN_NAME = "com.livk.commons.SpringContextHolder";

	private static final SpringIoC IOC = new SpringIoC();

	/**
	 * Spring事件发布
	 * @param event 事件
	 */
	public static <E extends ApplicationEvent> void publishEvent(E event) {
		IOC.unwrap().publishEvent(event);
	}

	/**
	 * 根据BeanName获取Bean
	 * @param <T> 泛型
	 * @param name BeanName
	 * @return spring bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) IOC.getBeanFactory().getBean(name);
	}

	/**
	 * 根据Bean Type获取Bean
	 * @param <T> 泛型
	 * @param typeClass 类型
	 * @return spring bean
	 */
	public static <T> T getBean(Class<T> typeClass) {
		return IOC.getBeanFactory().getBean(typeClass);
	}

	/**
	 * 根据BeanName和Bean Type获取Bean
	 * @param <T> 泛型
	 * @param name BeanName
	 * @param typeClass 类型
	 * @return spring bean
	 */
	public static <T> T getBean(String name, Class<T> typeClass) {
		return IOC.getBeanFactory().getBean(name, typeClass);
	}

	/**
	 * 根据Bean Type获取到ObjectFactory
	 * <p>
	 * 可用于延时加载
	 * @param <T> 泛型
	 * @param typeClass 类型
	 * @return spring bean provider
	 */
	public static <T> ObjectProvider<T> getBeanProvider(Class<T> typeClass) {
		return IOC.getBeanFactory().getBeanProvider(typeClass);
	}

	/**
	 * 根据ResolvableType获取到ObjectFactory
	 * <p>
	 * 可用于延时加载
	 * @param <T> 泛型
	 * @param resolvableType 类型相关
	 * @return spring bean provider
	 */
	public static <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType) {
		return IOC.getBeanFactory().getBeanProvider(resolvableType);
	}

	/**
	 * 根据Bean Type获取到map
	 * <p>
	 * key为BeanName, value为对应的spring bean
	 * @param <T> 泛型
	 * @param typeClass 类型
	 * @return spring bean map
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> typeClass) {
		return IOC.getBeanFactory().getBeansOfType(typeClass);
	}

	/**
	 * 根据key获取到相关的环境信息
	 * @param key key
	 * @return string
	 */
	public static String getProperty(String key) {
		return getProperty(key, String.class);
	}

	/**
	 * 根据key获取到相关的环境信息,并转化成对应的类型
	 * @param <T> 泛型
	 * @param key key
	 * @param requiredType 返回类型
	 * @return value
	 */
	public static <T> T getProperty(String key, Class<T> requiredType) {
		return IOC.unwrap().getEnvironment().getProperty(key, requiredType);
	}

	/**
	 * 根据key获取到相关的环境信息,并转化成对应的类型
	 * <p>
	 * 如果没有相关的key，则返回defaultValue
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
	 * 解析文本占位符${**}
	 * @param text string
	 * @return value
	 */
	public static String resolvePlaceholders(String text) {
		return IOC.unwrap().getEnvironment().resolvePlaceholders(text);
	}

	/**
	 * {@link Binder#get(Environment)}的补充、用户添加数据转换器
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
	 * 注册bean
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
	 * 注册bean
	 * @param beanDefinition bean定义
	 * @param beanName BeanName
	 */
	public static void registerBean(BeanDefinition beanDefinition, String beanName) {
		if (IOC.getBeanFactory() instanceof DefaultListableBeanFactory defaultBeanFactory) {
			registerBean(defaultBeanFactory, beanDefinition, beanName);
		}
		else if (IOC.unwrap() instanceof GenericApplicationContext context) {
			registerBean(context, beanDefinition, beanName);
		}
		else {
			throw new BeanCreationNotAllowedException(beanName, "missing created bean factory");
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
		SpringContextHolder.IOC.beanFactory(null);
		SpringContextHolder.IOC.applicationContext(null);
	}

	private static class SpringIoC implements GenericWrapper<ApplicationContext> {

		private volatile ApplicationContext applicationContext;

		private volatile BeanFactory beanFactory;

		private ListableBeanFactory getBeanFactory() {
			return beanFactory instanceof ListableBeanFactory listableBeanFactory ? listableBeanFactory
					: applicationContext;
		}

		@Override
		public ApplicationContext unwrap() {
			return beanFactory instanceof ApplicationContext contextFactory ? contextFactory : applicationContext;
		}

		public void beanFactory(BeanFactory beanFactory) {
			this.beanFactory = beanFactory;
		}

		public void applicationContext(ApplicationContext applicationContext) {
			this.applicationContext = applicationContext;
		}

	}

}
