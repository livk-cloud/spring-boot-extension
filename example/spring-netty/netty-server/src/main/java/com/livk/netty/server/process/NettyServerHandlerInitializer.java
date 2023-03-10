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
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                //空闲检测
                .addLast(new ServerIdleStateHandler())
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(NettyMessage.Message.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new NettyServerHandler());
    }
}
