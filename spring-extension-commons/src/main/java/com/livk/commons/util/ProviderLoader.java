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

package com.livk.commons.util;

import com.google.common.collect.Lists;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.ServiceLoader;

/**
 * SPI加载器
 *
 * @author livk
 * @see ServiceLoader
 * @see SpringFactoriesLoader
 */
public sealed interface ProviderLoader permits ProviderLoader.AbstractLoader {

	/**
	 * JDK SPI加载器实例
	 */
	ProviderLoader JDK_SERVICE = new AbstractLoader.JdkServiceLoader();

	/**
	 * Spring SPI加载器实例
	 */
	ProviderLoader SPRING_FACTORY = new AbstractLoader.SpringFactoryLoader();

	/**
	 * 根据type加载对应的实例
	 * @param <T> type parameter
	 * @param type type
	 * @return list
	 */
	<T> List<T> load(Class<T> type);

	/**
	 * 根据resolvableType加载对应的实例
	 * @param <T> type parameter
	 * @param resolvableType resolvable type
	 * @return list
	 */
	default <T> List<T> load(ResolvableType resolvableType) {
		Class<T> type = ClassUtils.toClass(resolvableType.getType());
		return load(type);
	}

	/**
	 * 抽象ProviderLoader
	 */
	abstract non-sealed class AbstractLoader implements ProviderLoader {

		@Override
		public final <T> List<T> load(Class<T> type) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			return load(type, classLoader);
		}

		/**
		 * 根据type加载对应的实例
		 * @param <T> type parameter
		 * @param type type
		 * @param classLoader classLoader
		 * @return list
		 */
		protected abstract <T> List<T> load(Class<T> type, ClassLoader classLoader);

		private static class JdkServiceLoader extends AbstractLoader {

			@Override
			protected <T> List<T> load(Class<T> type, ClassLoader classLoader) {
				return Lists.newArrayList(ServiceLoader.load(type, classLoader));
			}

		}

		private static class SpringFactoryLoader extends AbstractLoader {

			@Override
			protected <T> List<T> load(Class<T> type, ClassLoader classLoader) {
				return SpringFactoriesLoader.loadFactories(type, classLoader);
			}

		}

	}

}
