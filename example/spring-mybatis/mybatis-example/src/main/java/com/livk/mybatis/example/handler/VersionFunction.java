package com.livk.mybatis.example.handler;

import com.livk.autoconfigure.mybatis.inject.handler.FunctionHandle;

/**
 * <p>
 * VersionFunction
 * </p>
 *
 * @author livk
 */
public class VersionFunction implements FunctionHandle<Integer> {

    @Override
    public Integer handler() {
        return 0;
    }

}
