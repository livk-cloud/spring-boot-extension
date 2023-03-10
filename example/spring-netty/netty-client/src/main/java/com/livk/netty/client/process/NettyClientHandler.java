package com.livk.netty.client.process;

import com.livk.netty.commons.protobuf.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author livk
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<NettyMessage.Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage.Message msg) throws Exception {
        log.info("客户端收到消息：{}", msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
