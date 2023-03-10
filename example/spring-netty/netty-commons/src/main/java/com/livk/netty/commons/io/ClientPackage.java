package com.livk.netty.commons.io;

import com.livk.netty.commons.MessageType;

/**
 * @author livk
 */
public class ClientPackage extends AbstractPackage {
    @Override
    public Byte type() {
        return MessageType.HEARTBEAT_CLIENT;
    }
}
