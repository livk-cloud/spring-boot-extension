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

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author livk
 */
@Slf4j
public class ServerIdleStateHandler extends IdleStateHandler {

	/**
	 * 设置空闲检测时间为 30s
	 */
	private static final int READER_IDLE_TIME = 30;

	public ServerIdleStateHandler() {
		super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
	}

	@Override
	protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
		log.info("{} 秒内没有读取到数据,关闭连接", READER_IDLE_TIME);
		ctx.channel().close();
	}

}
