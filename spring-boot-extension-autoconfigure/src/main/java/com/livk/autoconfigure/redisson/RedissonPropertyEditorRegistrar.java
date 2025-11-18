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

package com.livk.autoconfigure.redisson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.commons.jackson.support.JacksonSupport;
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
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.CommandMapper;
import org.redisson.config.Config;
import org.redisson.config.CredentialsResolver;
import org.redisson.config.DecorrelatedJitterDelay;
import org.redisson.config.DelayStrategy;
import org.redisson.config.EqualJitterDelay;
import org.redisson.config.FullJitterDelay;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.ReplicatedServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.balancer.LoadBalancer;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.beans.PropertyEditorSupport;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author livk
 */
final class RedissonPropertyEditorRegistrar implements PropertyEditorRegistrar {

	private static final Map<Class<?>, Class<?>> REDISSON_MIXIN = Map.ofEntries(
			Map.entry(Config.class, ConfigMixIn.class),
			Map.entry(BaseMasterSlaveServersConfig.class, ConfigPropsMixIn.class),
			Map.entry(ReferenceCodecProvider.class, ClassMixIn.class),
			Map.entry(AddressResolverGroupFactory.class, ClassMixIn.class), Map.entry(Codec.class, ClassMixIn.class),
			Map.entry(RedissonNodeInitializer.class, ClassMixIn.class), Map.entry(LoadBalancer.class, ClassMixIn.class),
			Map.entry(NatMapper.class, ClassMixIn.class), Map.entry(NameMapper.class, ClassMixIn.class),
			Map.entry(NettyHook.class, ClassMixIn.class), Map.entry(CredentialsResolver.class, ClassMixIn.class),
			Map.entry(EventLoopGroup.class, ClassMixIn.class), Map.entry(ConnectionListener.class, ClassMixIn.class),
			Map.entry(ExecutorService.class, ClassMixIn.class), Map.entry(KeyManagerFactory.class, IgnoreMixIn.class),
			Map.entry(TrustManagerFactory.class, IgnoreMixIn.class), Map.entry(CommandMapper.class, ClassMixIn.class),
			Map.entry(FailedNodeDetector.class, ClassMixIn.class), Map.entry(DelayStrategy.class, ClassMixIn.class),
			Map.entry(EqualJitterDelay.class, DelayMixin.class), Map.entry(FullJitterDelay.class, DelayMixin.class),
			Map.entry(DecorrelatedJitterDelay.class, DelayMixin.class));

	private static final JacksonSupport support;

	static {
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("classFilter",
				SimpleBeanPropertyFilter.filterOutAllExcept());

		JsonInclude.Value value = JsonInclude.Value.construct(JsonInclude.Include.NON_NULL,
				JsonInclude.Include.NON_NULL);

		YAMLMapper.Builder builder = YAMLMapper.builder()
			.addModule(new JavaTimeModule())
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.defaultPropertyInclusion(value)
			.filterProvider(filterProvider);
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
	private static final class RedissonTypePropertyEditor extends PropertyEditorSupport {

		private final Class<?> type;

		@Override
		public void setAsText(String text) {
			setValue(support.readValue(text, type));
		}

	}

	@JsonIgnoreType
	private static final class IgnoreMixIn {

	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "class")
	@JsonFilter("classFilter")
	public static final class ClassMixIn {

	}

	@JsonIgnoreProperties({ "slaveNotUsed" })
	private static final class ConfigPropsMixIn {

	}

	private static final class DelayMixin {

		@JsonCreator
		DelayMixin(@JsonProperty("baseDelay") Duration baseDelay, @JsonProperty("maxDelay") Duration maxDelay) {
		}

	}

	@JsonIgnoreProperties({ "clusterConfig", "sentinelConfig", "singleConfig" })
	private static final class ConfigMixIn {

		@JsonProperty
		SentinelServersConfig sentinelServersConfig;

		@JsonProperty
		MasterSlaveServersConfig masterSlaveServersConfig;

		@JsonProperty
		SingleServerConfig singleServerConfig;

		@JsonProperty
		ClusterServersConfig clusterServersConfig;

		@JsonProperty
		ReplicatedServersConfig replicatedServersConfig;

	}

}
