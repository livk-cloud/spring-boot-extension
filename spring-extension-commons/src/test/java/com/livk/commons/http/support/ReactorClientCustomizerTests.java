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

package com.livk.commons.http.support;

import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.http.client.reactive.ReactorClientHttpConnectorBuilder;
import org.springframework.http.client.ReactorResourceFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ReactorClientCustomizerTests {

	final ReactorResourceFactory resourceFactory = new ReactorResourceFactory();

	{
		resourceFactory.setUseGlobalResources(false);
		resourceFactory.afterPropertiesSet();
	}

	// --- fluent configuration ---

	@Test
	void withConnectTimeoutReturnsSameInstance() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory);
		assertThat(customizer.withConnectTimeout(5000)).isSameAs(customizer);
	}

	@Test
	void withResponseTimeoutReturnsSameInstance() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory);
		assertThat(customizer.withResponseTimeout(30)).isSameAs(customizer);
	}

	@Test
	void withReadTimeoutReturnsSameInstance() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory);
		assertThat(customizer.withReadTimeout(10)).isSameAs(customizer);
	}

	@Test
	void withWriteTimeoutReturnsSameInstance() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory);
		assertThat(customizer.withWriteTimeout(10)).isSameAs(customizer);
	}

	// --- addChannelHandler ---

	@Test
	void addChannelHandlerReturnsSameInstance() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory);
		ChannelHandler handler = new IdleStateHandler(0, 0, 60);
		assertThat(customizer.addChannelHandler(handler)).isSameAs(customizer);
	}

	@Test
	void addChannelHandlerIgnoresNull() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory);
		customizer.addChannelHandler((ChannelHandler) null);
		// should not throw, null is silently ignored
	}

	@Test
	void addChannelHandlerIgnoresDuplicate() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory);
		ChannelHandler handler = new IdleStateHandler(0, 0, 60);
		customizer.addChannelHandler(handler);
		customizer.addChannelHandler(handler);
		// duplicate is silently ignored
	}

	// --- customize ---

	@Test
	void customizeReturnsNonNullBuilder() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory);
		ReactorClientHttpConnectorBuilder result = customizer
			.customize(org.springframework.boot.http.client.reactive.ClientHttpConnectorBuilder.reactor());
		assertThat(result).isNotNull();
	}

	@Test
	void customizeWithAllOptionsDoesNotThrow() {
		ReactorClientCustomizer customizer = new ReactorClientCustomizer(resourceFactory).withConnectTimeout(3000)
			.withResponseTimeout(15)
			.withReadTimeout(20)
			.withWriteTimeout(20);
		ReactorClientHttpConnectorBuilder result = customizer
			.customize(org.springframework.boot.http.client.reactive.ClientHttpConnectorBuilder.reactor());
		assertThat(result).isNotNull();
	}

}
