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

package com.livk.netty.client.process;

import com.livk.netty.commons.protobuf.NettyMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

/**
 * @author livk
 */
@Slf4j
public class NettyClient implements AutoCloseable {

	private final EventLoopGroup group = new NioEventLoopGroup();

	@Value("${spring.netty.port}")
	private int port;

	@Value("${spring.netty.host}")
	private String host;

	private SocketChannel socketChannel;

	public ChannelFuture sendMsg(NettyMessage.Message message) {
		return socketChannel.writeAndFlush(message);
	}

	public void start() {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.remoteAddress(host, port)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ClientHandlerInitializer(this));
		ChannelFuture future = bootstrap.connect();
		future.addListener((ChannelFutureListener) channelFuture -> {
			if (channelFuture.isSuccess()) {
				log.info("连接Netty服务端成功");
			}
			else {
				log.info("连接失败，进行断线重连");
				try (EventLoop eventLoop = channelFuture.channel().eventLoop()) {
					eventLoop.schedule(this::start, 20, TimeUnit.SECONDS);
				}
			}
		});
		socketChannel = (SocketChannel) future.channel();
	}

	@Override
	public void close() throws InterruptedException {
		group.shutdownGracefully().sync();
	}

}
