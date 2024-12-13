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

package com.livk.autoconfigure.redisson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.collect.Sets;
import com.livk.commons.jackson.core.JacksonSupport;
import com.livk.commons.util.ClassUtils;
import io.netty.channel.EventLoopGroup;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.redisson.api.NameMapper;
import org.redisson.api.NatMapper;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.codec.ReferenceCodecProvider;
import org.redisson.config.BaseMasterSlaveServersConfig;
import org.redisson.config.CommandMapper;
import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import org.redisson.config.CredentialsResolver;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.balancer.LoadBalancer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * PropertyEditorProxySupport
 * </p>
 *
 * @author livk
 */
@UtilityClass
final class PropertyEditorProxySupport {

	private static final String PACKAGE_NAME = PropertyEditorProxySupport.class.getPackageName();

	private static final JacksonSupport support = new JacksonSupport(createMapper());

	private static final Set<Class<?>> supportType = Sets.newHashSet(Config.class, AddressResolverGroupFactory.class,
			Codec.class, RedissonNodeInitializer.class, LoadBalancer.class, NatMapper.class, NameMapper.class,
			NettyHook.class, CredentialsResolver.class, EventLoopGroup.class, ConnectionListener.class,
			ExecutorService.class, KeyManagerFactory.class, TrustManagerFactory.class, CommandMapper.class);

	/**
	 * Proxy converters set.
	 * @return the set
	 */
	public static Map<Class<?>, Class<? extends PropertyEditor>> make() {
		return supportType.stream()
			.collect(Collectors.toMap(Function.identity(), PropertyEditorProxySupport::makeProxyEditor));
	}

	private static Class<? extends PropertyEditor> makeProxyEditor(Class<?> type) {
		String name = PACKAGE_NAME + "." + type.getSimpleName() + PropertyEditor.class.getSimpleName() + "$Proxy";
		try (DynamicType.Unloaded<PropertyEditorSupport> unloaded = new ByteBuddy()
			.subclass(PropertyEditorSupport.class)
			.name(name)
			.method(ElementMatchers.named("setAsText"))
			.intercept(MethodDelegation.to(new SetAsTextInterceptor(type)))
			.make()) {
			return unloaded.load(ClassUtils.getDefaultClassLoader()).getLoaded();
		}
	}

	@RequiredArgsConstructor
	public class SetAsTextInterceptor {

		private final Class<?> type;

		@SuppressWarnings("unused")
		public void intercept(String text, PropertyEditorSupport instance) {
			try {
				instance.setValue(support.readValue(text, type));
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Failed to set property value", e);
			}
		}

	}

	private static YAMLMapper createMapper() {
		YAMLMapper mapper = new YAMLMapper();
		mapper.addMixIn(Config.class, ConfigSupport.ConfigMixIn.class);
		mapper.addMixIn(BaseMasterSlaveServersConfig.class, ConfigSupport.ConfigPropsMixIn.class);
		mapper.addMixIn(ReferenceCodecProvider.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(AddressResolverGroupFactory.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(Codec.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(RedissonNodeInitializer.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(LoadBalancer.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(NatMapper.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(NameMapper.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(NettyHook.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(CredentialsResolver.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(EventLoopGroup.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(ConnectionListener.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(ExecutorService.class, ConfigSupport.ClassMixIn.class);
		mapper.addMixIn(KeyManagerFactory.class, ConfigSupport.IgnoreMixIn.class);
		mapper.addMixIn(TrustManagerFactory.class, ConfigSupport.IgnoreMixIn.class);
		mapper.addMixIn(CommandMapper.class, ConfigSupport.ClassMixIn.class);
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("classFilter",
				SimpleBeanPropertyFilter.filterOutAllExcept());
		mapper.setFilterProvider(filterProvider);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return mapper;
	}

}
