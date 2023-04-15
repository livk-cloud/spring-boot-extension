package com.livk.autoconfigure.mybatis.inject.handler.time;

import com.livk.autoconfigure.mybatis.inject.handler.FunctionHandle;

/**
 * <p>
 * TimestampFunction
 * </p>
 *
 * @author livk
 */
public class TimestampFunction implements FunctionHandle<Long> {

    @Override
    public Long handler() {
        return System.currentTimeMillis();
    }

}
