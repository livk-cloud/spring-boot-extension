package com.livk.example.handler;

import com.livk.autoconfigure.mybatis.handler.FunctionHandle;

/**
 * <p>
 * VersionFunction
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
public class VersionFunction implements FunctionHandle<Integer> {

    @Override
    public Integer handler() {
        return 0;
    }

}
