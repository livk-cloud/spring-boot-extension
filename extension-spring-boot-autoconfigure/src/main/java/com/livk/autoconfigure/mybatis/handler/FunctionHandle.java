package com.livk.autoconfigure.mybatis.handler;

import org.springframework.core.GenericTypeResolver;

/**
 * <p>
 * FunctionHandle
 * </p>
 *
 * @author livk
 *
 */
public interface FunctionHandle<T> {

    T handler();

    @SuppressWarnings("unchecked")
    default Class<T> getType() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), FunctionHandle.class);
    }
}
