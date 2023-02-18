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
                        .setContent("heartbeat").build();
                ctx.writeAndFlush(heartbeat).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(nettyClient::start, 10L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("message：{}", cause.getMessage(), cause);
        ctx.channel().close();
    }
}
