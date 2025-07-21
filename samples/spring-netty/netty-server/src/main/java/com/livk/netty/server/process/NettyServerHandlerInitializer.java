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

import com.livk.netty.commons.protobuf.NettyMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author livk
 */
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) {
		ch.pipeline()
			// 空闲检测
			.addLast(new ServerIdleStateHandler())
			.addLast(new ProtobufVarint32FrameDecoder())
			.addLast(new ProtobufDecoder(NettyMessage.Message.getDefaultInstance()))
			.addLast(new ProtobufVarint32LengthFieldPrepender())
			.addLast(new ProtobufEncoder())
			.addLast(new NettyServerHandler());
	}

}
