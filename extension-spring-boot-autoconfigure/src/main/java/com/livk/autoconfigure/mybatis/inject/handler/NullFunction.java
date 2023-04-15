package com.livk.autoconfigure.mybatis.inject.handler;

/**
 * <p>
 * NullFunction
 * </p>
 *
 * @author livk
 */
public class NullFunction implements FunctionHandle<Object> {

    @Override
    public Object handler() {
        return null;
    }

}
