/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.redisearch;

import com.livk.commons.util.ClassUtils;
import com.livk.commons.util.GenericsByteBuddy;
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

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author livk
 */
final class FactoryProxySupport {

	private static final Map<Class<? extends AbstractRedisClient>, Class<? extends RediSearchConnectionFactory>> FACTORY_CACHE = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T extends AbstractRedisClient, S extends RediSearchConnectionFactory> S newProxy(T client) {
		Class<T> clientType = (Class<T>) client.getClass();
		Class<S> type = (Class<S>) FACTORY_CACHE.computeIfAbsent(clientType, FactoryProxySupport::createFactoryClass);
		try {
			return type.getConstructor(clientType).newInstance(client);
		}
		catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static <S extends RediSearchConnectionFactory> Class<? extends S> createFactoryClass(
			Class<? extends AbstractRedisClient> clientType) {
		TypeDescription definitions = TypeDescription.ForLoadedType.of(RediSearchConnectionFactory.class);
		try (DynamicType.Unloaded<S> unloaded = new GenericsByteBuddy().<S>subType(definitions)
			.name(FactoryProxySupport.class.getPackageName() + "." + clientType.getSimpleName()
					+ "$ProxyConnectionFactory")
			.defineField("client", clientType, Modifier.PRIVATE | Modifier.FINAL)
			.defineConstructor(Modifier.PUBLIC)
			.withParameters(clientType)
			.intercept(MethodCall.invoke(Object.class.getConstructor())
				.andThen(FieldAccessor.ofField("client").setsArgumentAt(0)))
			.method(ElementMatchers.named("connect").and(ElementMatchers.takesArguments(0)))
			.intercept(MethodDelegation.to(ConnectInterceptor.class))
			.method(ElementMatchers.named("connect").and(ElementMatchers.takesArguments(1)))
			.intercept(MethodDelegation.to(ConnectWithCodecInterceptor.class))
			.method(ElementMatchers.named("close"))
			.intercept(MethodDelegation.to(CloseInterceptor.class))
			.make()) {
			return unloaded.load(ClassUtils.getDefaultClassLoader(), ClassLoadingStrategy.Default.INJECTION)
				.getLoaded();
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static class ConnectInterceptor {

		@SuppressWarnings("unchecked")
		@RuntimeType
		public static StatefulRedisModulesConnection<String, String> connect(@FieldValue("client") Object client)
				throws Exception {
			return (StatefulRedisModulesConnection<String, String>) client.getClass()
				.getMethod("connect")
				.invoke(client);
		}

	}

	private static class ConnectWithCodecInterceptor {

		@SuppressWarnings("unchecked")
		@RuntimeType
		public static <K, V> StatefulRedisModulesConnection<K, V> connect(@FieldValue("client") Object client,
				@Argument(0) RedisCodec<K, V> codec) throws Exception {
			return (StatefulRedisModulesConnection<K, V>) client.getClass()
				.getMethod("connect", RedisCodec.class)
				.invoke(client, codec);
		}

	}

	private static class CloseInterceptor {

		@RuntimeType
		public static void close(@FieldValue("client") AbstractRedisClient client) {
			client.close();
		}

	}

}
