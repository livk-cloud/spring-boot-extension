package com.livk.commons.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.core.GenericTypeResolver;

/**
 * The interface Delegating wrapper.
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface GenericWrapper<T> extends Wrapper {

    /**
     * Of delegating wrapper.
     *
     * @param <T>   the type parameter
     * @param value the value
     * @return the delegating wrapper
     */
    static <T> GenericWrapper<T> of(T value) {
        return new SimpleWrapper<>(value);
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
    T unwrap();

    /**
     * The type Simple wrapper.
     *
     * @param <T> the type parameter
     */
    @RequiredArgsConstructor
    class SimpleWrapper<T> implements GenericWrapper<T> {

        private final T value;

        @Override
        public T unwrap() {
            return value;
        }
    }
}
