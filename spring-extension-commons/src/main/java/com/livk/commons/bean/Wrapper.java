package com.livk.commons.bean;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Wrapper
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface Wrapper<T> {
    /**
     * Of wrapper.
     *
     * @param <T>   the type parameter
     * @param value the t
     * @return the wrapper
     */
    static <T> Wrapper<T> of(T value) {
        return new SimpleWrapper<>(value);
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
    class SimpleWrapper<T> implements Wrapper<T> {

        private final T value;

        @Override
        public T unwrap() {
            return value;
        }
    }
}
