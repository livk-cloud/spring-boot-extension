package com.livk.netty.commons.codec;

import com.livk.netty.commons.io.ClientPackage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author livk
 */
public class HeartbeatEncoder extends MessageToByteEncoder<ClientPackage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ClientPackage msg, ByteBuf out) throws Exception {
        out.writeByte(msg.getVersion());
        out.writeByte(msg.type());
    }
}
