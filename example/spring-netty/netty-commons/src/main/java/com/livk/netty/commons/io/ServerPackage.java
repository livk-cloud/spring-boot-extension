package com.livk.netty.commons.io;

import com.livk.netty.commons.MessageType;

/**
 * @author livk
 */
public class ServerPackage extends AbstractPackage {
    @Override
    public Byte type() {
        return MessageType.HEARTBEAT_SERVER;
    }
}
