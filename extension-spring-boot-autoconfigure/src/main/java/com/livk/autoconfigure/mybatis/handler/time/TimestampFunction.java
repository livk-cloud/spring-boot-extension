package com.livk.autoconfigure.mybatis.handler.time;

import com.livk.autoconfigure.mybatis.handler.FunctionHandle;

/**
 * <p>
 * TimestampFunction
 * </p>
 *
 * @author livk
 * 
 */
public class TimestampFunction implements FunctionHandle<Long> {

    @Override
    public Long handler() {
        return System.currentTimeMillis();
    }

}
