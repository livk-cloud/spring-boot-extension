package com.livk.netty.commons.io;

import lombok.Data;

/**
 * @author livk
 */
@Data
public abstract class AbstractPackage {

    protected Byte version = 1;

    public abstract Byte type();
}
