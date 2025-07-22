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
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

	private final NettyClient nettyClient;

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent idleStateEvent) {
			if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
				NettyMessage.Message heartbeat = NettyMessage.Message.newBuilder()
					.setType(NettyMessage.Message.MessageType.HEARTBEAT_CLIENT)
					.setRequestId(UUID.randomUUID().toString())
					.setContent("heartbeat")
					.build();
				ctx.writeAndFlush(heartbeat).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			}
		}
		else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelInactive(@NonNull ChannelHandlerContext ctx) throws Exception {
		try (EventLoop eventLoop = ctx.channel().eventLoop()) {
			eventLoop.schedule(nettyClient::start, 10L, TimeUnit.SECONDS);
			super.channelInactive(ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("message：{}", cause.getMessage(), cause);
		ctx.channel().close();
	}

}
