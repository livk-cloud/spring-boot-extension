package com.livk.netty.client.process;

import com.livk.netty.commons.protobuf.NettyMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class ClientHandlerInitializer extends ChannelInitializer<Channel> {

    private final NettyClient nettyClient;

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                .addLast(new IdleStateHandler(0, 10, 0))
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(NettyMessage.Message.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new HeartbeatHandler(nettyClient))
                .addLast(new NettyClientHandler());
    }
}
