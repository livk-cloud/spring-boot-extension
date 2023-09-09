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
 * The interface Loader.
 *
 * @author livk
 */
public sealed interface ProviderLoader permits ProviderLoader.AbstractLoader {

	/**
	 * The constant JDK_SERVICE.
	 */
	ProviderLoader JDK_SERVICE = new AbstractLoader.JdkServiceLoader();

	/**
	 * The constant SPRING_FACTORY.
	 */
	ProviderLoader SPRING_FACTORY = new AbstractLoader.SpringFactoryLoader();

	/**
	 * Load stream.
	 *
	 * @param <T>  the type parameter
	 * @param type the type
	 * @return the stream
	 */
	<T> List<T> load(Class<T> type);

	/**
	 * Load list.
	 *
	 * @param <T>            the type parameter
	 * @param resolvableType the resolvable type
	 * @return the list
	 */
	default <T> List<T> load(ResolvableType resolvableType) {
		Class<T> type = ClassUtils.toClass(resolvableType.getType());
		return load(type);
	}

	/**
	 * The type Abstract loader.
	 */
	abstract non-sealed class AbstractLoader implements ProviderLoader {

		@Override
		public final <T> List<T> load(Class<T> type) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			return load(type, classLoader);
		}

		/**
		 * Load list.
		 *
		 * @param <T>         the type parameter
		 * @param type        the type
		 * @param classLoader the class loader
		 * @return the list
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
