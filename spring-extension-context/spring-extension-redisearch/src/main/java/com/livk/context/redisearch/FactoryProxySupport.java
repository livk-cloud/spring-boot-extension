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

package com.livk.context.redisearch;

import com.livk.commons.util.ClassUtils;
import com.livk.commons.util.GenericsByteBuddy;
import com.livk.commons.util.ReflectionUtils;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.codec.RedisCodec;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author livk
 */
final class FactoryProxySupport {

	private static final Map<Class<? extends AbstractRedisClient>, Constructor<? extends RediSearchConnectionFactory>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

	private static final Map<Class<?>, Method> CONNECT_METHOD_CACHE = new ConcurrentHashMap<>();

	private static final Constructor<Object> OBJECT_CONSTRUCTOR;

	static {
		try {
			OBJECT_CONSTRUCTOR = Object.class.getConstructor();
		}
		catch (NoSuchMethodException ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractRedisClient, S extends RediSearchConnectionFactory> S newProxy(T client) {
		Class<T> clientType = (Class<T>) client.getClass();
		Constructor<S> constructor = (Constructor<S>) CONSTRUCTOR_CACHE.computeIfAbsent(clientType,
				FactoryProxySupport::createFactoryAndCacheConstructor);
		try {
			return constructor.newInstance(client);
		}
		catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	private static Constructor<? extends RediSearchConnectionFactory> createFactoryAndCacheConstructor(
			Class<? extends AbstractRedisClient> clientType) {
		Class<? extends RediSearchConnectionFactory> factoryClass = createFactoryClass(clientType);
		try {
			return factoryClass.getConstructor(clientType);
		}
		catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	static Method resolveConnectMethod(Class<?> clientType) {
		return CONNECT_METHOD_CACHE.computeIfAbsent(clientType,
				type -> ClassUtils.getMethod(type, "connect", RedisCodec.class));
	}

	private static <S extends RediSearchConnectionFactory> Class<? extends S> createFactoryClass(
			Class<? extends AbstractRedisClient> clientType) {
		TypeDescription definitions = TypeDescription.ForLoadedType.of(RediSearchConnectionFactory.class);
		try (DynamicType.Unloaded<S> unloaded = new GenericsByteBuddy().<S>subclass(definitions)
			.name(FactoryProxySupport.class.getPackageName() + "." + clientType.getSimpleName()
					+ "$ProxyConnectionFactory")
			.defineField("client", clientType, Modifier.PRIVATE | Modifier.FINAL)
			.defineConstructor(Modifier.PUBLIC)
			.withParameters(clientType)
			.intercept(MethodCall.invoke(OBJECT_CONSTRUCTOR).andThen(FieldAccessor.ofField("client").setsArgumentAt(0)))
			.method(ElementMatchers.named("connect").and(ElementMatchers.takesArguments(1)))
			.intercept(MethodDelegation.to(ConnectWithCodecInterceptor.class))
			.method(ElementMatchers.named("close"))
			.intercept(MethodDelegation.to(CloseInterceptor.class))
			.make()) {
			return unloaded.load(ClassUtils.getDefaultClassLoader(), ClassLoadingStrategy.Default.INJECTION)
				.getLoaded();
		}
	}

	private static final class ConnectWithCodecInterceptor {

		@SuppressWarnings({ "unchecked", "unused" })
		@RuntimeType
		public static <K, V> StatefulRedisModulesConnection<K, V> connect(@FieldValue("client") Object client,
				@Argument(0) RedisCodec<K, V> codec) {
			Method connect = resolveConnectMethod(client.getClass());
			return (StatefulRedisModulesConnection<K, V>) ReflectionUtils.invokeMethod(connect, client, codec);
		}

	}

	private static final class CloseInterceptor {

		@SuppressWarnings("unused")
		@RuntimeType
		public static void close(@FieldValue("client") AbstractRedisClient client) {
			client.close();
		}

	}

}
