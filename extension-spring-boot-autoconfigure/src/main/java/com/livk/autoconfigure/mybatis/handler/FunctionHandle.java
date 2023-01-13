package com.livk.autoconfigure.mybatis.handler;

import org.springframework.core.GenericTypeResolver;

/**
 * <p>
 * FunctionHandle
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface FunctionHandle<T> {

    /**
     * Handler t.
     *
     * @return the t
     */
    T handler();

    /**
     * Gets type.
     *
     * @return the type
     */
    @SuppressWarnings("unchecked")
    default Class<T> getType() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), FunctionHandle.class);
    }
}
