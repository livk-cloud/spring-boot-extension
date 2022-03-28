package com.livk.handler;

/**
 * <p>
 * NullFunction
 * </p>
 *
 * @author livk
 * @date 2022/3/28
 */
public class NullFunction implements FunctionHandle<Object> {
    @Override
    public Object handler() {
        return null;
    }
}
