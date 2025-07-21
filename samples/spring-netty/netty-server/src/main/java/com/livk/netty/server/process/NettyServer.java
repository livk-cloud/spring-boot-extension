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

package com.livk.netty.server.process;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetSocketAddress;

/**
 * @author livk
 */
@Slf4j
public class NettyServer implements AutoCloseable {

	private final EventLoopGroup boss = new NioEventLoopGroup();

	private final EventLoopGroup work = new NioEventLoopGroup();

	@Value("${spring.netty.server.port}")
	private Integer port;

	public void start() throws InterruptedException {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, work)
			// 指定Channel
			.channel(NioServerSocketChannel.class)
			// 使用指定的端口设置套接字地址
			.localAddress(new InetSocketAddress(port))

			// 服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
			.option(ChannelOption.SO_BACKLOG, 1024)

			// 设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
			.childOption(ChannelOption.SO_KEEPALIVE, true)

			// 将小的数据包包装成更大的帧进行传送，提高网络的负载
			.childOption(ChannelOption.TCP_NODELAY, true)

			.childHandler(new NettyServerHandlerInitializer());
		ChannelFuture future = bootstrap.bind().sync();
		if (future.isSuccess()) {
			log.info("启动 Netty Server");
		}
	}

	@Override
	public void close() throws InterruptedException {
		boss.shutdownGracefully().sync();
		work.shutdownGracefully().sync();
	}

}
