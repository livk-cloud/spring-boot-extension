package com.livk.netty.commons.codec;

import com.livk.netty.commons.io.ClientPackage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author livk
 */
public class HeartbeatDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte version = in.readByte();
        byte type = in.readByte();
        ClientPackage clientPackage = new ClientPackage();
        clientPackage.setVersion(version);
        out.add(clientPackage);
    }
}
