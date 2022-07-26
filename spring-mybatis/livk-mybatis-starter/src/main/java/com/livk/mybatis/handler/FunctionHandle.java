package com.livk.mybatis.handler;

import org.springframework.core.GenericTypeResolver;

/**
 * <p>
 * FunctionHandle
 * </p>
 *
 * @author livk
 * @date 2022/1/29
 */
public interface FunctionHandle<T> {

    T handler();

    @SuppressWarnings("unchecked")
    default Class<T> getType() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), FunctionHandle.class);
    }
}
