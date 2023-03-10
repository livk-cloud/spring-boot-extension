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
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage.Message msg) throws Exception {
        if (msg.getType().equals(NettyMessage.Message.MessageType.HEARTBEAT_CLIENT)) {
            log.info("收到客户端发来的心跳消息：{}", msg);
            //回应pong
            ctx.writeAndFlush(new ServerPackage());
        } else if (msg.getType().equals(NettyMessage.Message.MessageType.NORMAL)) {
            log.info("收到客户端的业务消息：{}", msg.toString());
        }
    }
}
