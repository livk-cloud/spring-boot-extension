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
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.http.client.autoconfigure.reactive.ClientHttpConnectorBuilderCustomizer;
import org.springframework.boot.http.client.reactive.ReactorClientHttpConnectorBuilder;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.tcp.DefaultSslContextSpec;
import reactor.netty.tcp.SslProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 */
@RequiredArgsConstructor
public final class ReactorClientCustomizer
		implements ClientHttpConnectorBuilderCustomizer<ReactorClientHttpConnectorBuilder> {

	private int connectTimeout = 3_000;

	private int responseTimeout = 15;

	private int readTimeout = 20;

	private int writeTimeout = 20;

	private final List<ChannelHandler> handlerList = new ArrayList<>();

	private final ReactorResourceFactory reactorResourceFactory;

	public ReactorClientCustomizer withConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public ReactorClientCustomizer withResponseTimeout(Integer responseTimeout) {
		this.responseTimeout = responseTimeout;
		return this;
	}

	public ReactorClientCustomizer withReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	public ReactorClientCustomizer withWriteTimeout(Integer writeTimeout) {
		this.writeTimeout = writeTimeout;
		return this;
	}

	public ReactorClientCustomizer addChannelHandler(ChannelHandler... handlers) {
		for (ChannelHandler handler : handlers) {
			if (!handlerList.contains(handler) && handler != null) {
				handlerList.add(handler);
			}
		}
		return this;
	}

	@Override
	public ReactorClientHttpConnectorBuilder customize(ReactorClientHttpConnectorBuilder builder) {
		SslProvider.GenericSslContextSpec<SslContextBuilder> spec = DefaultSslContextSpec.forClient();
		return builder.withHttpClientCustomizer(httpClient -> httpClient
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
			.runOn(reactorResourceFactory.getLoopResources())
			.wiretap(WebClient.class.getName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL, StandardCharsets.UTF_8)
			.responseTimeout(Duration.ofSeconds(responseTimeout))
			.secure(sslContextSpec -> sslContextSpec.sslContext(spec))
			.doOnConnected(connection -> getHandlerList().forEach(connection::addHandlerLast)));
	}

	private List<ChannelHandler> getHandlerList() {
		handlerList.addFirst(new ReadTimeoutHandler(readTimeout));
		handlerList.addFirst(new WriteTimeoutHandler(writeTimeout));
		return handlerList;
	}

}
