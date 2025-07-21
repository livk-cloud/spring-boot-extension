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

import com.livk.netty.commons.io.ServerPackage;
import com.livk.netty.commons.protobuf.NettyMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author livk
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<NettyMessage.Message> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, NettyMessage.Message msg) {
		if (msg.getType().equals(NettyMessage.Message.MessageType.HEARTBEAT_CLIENT)) {
			log.info("收到客户端发来的心跳消息：{}", msg);
			// 回应pong
			ctx.writeAndFlush(new ServerPackage());
		}
		else if (msg.getType().equals(NettyMessage.Message.MessageType.NORMAL)) {
			log.info("收到客户端的业务消息：{}", msg);
		}
	}

}
