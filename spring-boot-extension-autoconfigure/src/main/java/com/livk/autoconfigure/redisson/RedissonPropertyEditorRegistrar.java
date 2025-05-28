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

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.livk.commons.jackson.core.JacksonSupport;
import io.netty.channel.EventLoopGroup;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.redisson.api.NameMapper;
import org.redisson.api.NatMapper;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.client.FailedNodeDetector;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.codec.ReferenceCodecProvider;
import org.redisson.config.BaseMasterSlaveServersConfig;
import org.redisson.config.CommandMapper;
import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import org.redisson.config.CredentialsResolver;
import org.redisson.config.DecorrelatedJitterDelay;
import org.redisson.config.DelayStrategy;
import org.redisson.config.EqualJitterDelay;
import org.redisson.config.FullJitterDelay;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.balancer.LoadBalancer;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.beans.PropertyEditorSupport;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author livk
 */
final class RedissonPropertyEditorRegistrar implements PropertyEditorRegistrar {

	private static final Map<Class<?>, Class<?>> REDISSON_MIXIN = Map.ofEntries(
			Map.entry(Config.class, ConfigSupport.ConfigMixIn.class),
			Map.entry(BaseMasterSlaveServersConfig.class, ConfigSupport.ConfigPropsMixIn.class),
			Map.entry(ReferenceCodecProvider.class, ConfigSupport.ClassMixIn.class),
			Map.entry(AddressResolverGroupFactory.class, ConfigSupport.ClassMixIn.class),
			Map.entry(Codec.class, ConfigSupport.ClassMixIn.class),
			Map.entry(RedissonNodeInitializer.class, ConfigSupport.ClassMixIn.class),
			Map.entry(LoadBalancer.class, ConfigSupport.ClassMixIn.class),
			Map.entry(NatMapper.class, ConfigSupport.ClassMixIn.class),
			Map.entry(NameMapper.class, ConfigSupport.ClassMixIn.class),
			Map.entry(NettyHook.class, ConfigSupport.ClassMixIn.class),
			Map.entry(CredentialsResolver.class, ConfigSupport.ClassMixIn.class),
			Map.entry(EventLoopGroup.class, ConfigSupport.ClassMixIn.class),
			Map.entry(ConnectionListener.class, ConfigSupport.ClassMixIn.class),
			Map.entry(ExecutorService.class, ConfigSupport.ClassMixIn.class),
			Map.entry(KeyManagerFactory.class, ConfigSupport.IgnoreMixIn.class),
			Map.entry(TrustManagerFactory.class, ConfigSupport.IgnoreMixIn.class),
			Map.entry(CommandMapper.class, ConfigSupport.ClassMixIn.class),
			Map.entry(FailedNodeDetector.class, ConfigSupport.ClassMixIn.class),
			Map.entry(DelayStrategy.class, ConfigSupport.ClassMixIn.class),
			Map.entry(EqualJitterDelay.class, ConfigSupport.DelayMixin.class),
			Map.entry(FullJitterDelay.class, ConfigSupport.DelayMixin.class),
			Map.entry(DecorrelatedJitterDelay.class, ConfigSupport.DelayMixin.class));

	private static final JacksonSupport support;

	static {
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("classFilter",
				SimpleBeanPropertyFilter.filterOutAllExcept());

		YAMLMapper.Builder builder = YAMLMapper.builder().filterProvider(filterProvider);
		REDISSON_MIXIN.forEach(builder::addMixIn);

		support = new JacksonSupport(builder.build());
	}

	@Override
	public void registerCustomEditors(@NonNull PropertyEditorRegistry registry) {
		for (Class<?> type : REDISSON_MIXIN.keySet()) {
			registry.registerCustomEditor(type, new RedissonTypePropertyEditor(type));
		}
	}

	@RequiredArgsConstructor
	private static class RedissonTypePropertyEditor extends PropertyEditorSupport {

		private final Class<?> type;

		@Override
		public void setAsText(String text) {
			setValue(support.readValue(text, type));
		}

	}

}
