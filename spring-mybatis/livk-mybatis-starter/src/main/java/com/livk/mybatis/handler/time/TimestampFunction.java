package com.livk.mybatis.handler.time;

import com.livk.mybatis.handler.FunctionHandle;

/**
 * <p>
 * TimestampFunction
 * </p>
 *
 * @author livk
 * @date 2022/3/28
 */
public class TimestampFunction implements FunctionHandle<Long> {

    @Override
    public Long handler() {
        return System.currentTimeMillis();
    }

}
