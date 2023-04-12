package com.livk.commons.bean;

import org.springframework.core.GenericTypeResolver;

/**
 * The interface Delegating wrapper.
 *
 * @param <V> the type parameter
 * @author livk
 */
public interface GenericWrapper<V> extends Wrapper {

    /**
     * Of delegating wrapper.
     *
     * @param <V>   the type parameter
     * @param value the value
     * @return the delegating wrapper
     */
    static <V> GenericWrapper<V> of(V value) {
        return new RecordWrapper<>(value);
    }

    @Override
    default boolean isWrapperFor(Class<?> type) {
        Class<?> typeArgument = GenericTypeResolver.resolveTypeArgument(this.getClass(), GenericWrapper.class);
        return typeArgument == null || type.equals(typeArgument);
    }

    @Override
    default <T> T unwrap(Class<T> type) {
        return type.cast(unwrap());
    }

    /**
     * Unwrap t.
     *
     * @return the t
     */
    V unwrap();
}
