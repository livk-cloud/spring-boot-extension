package com.livk.commons.bean;

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
     * @param <T> the type parameter
     * @param t   the t
     * @return the wrapper
     */
    static <T> Wrapper<T> of(T t) {
        return new WrapperProxy<>(t).unwrap();
    }

    /**
     * Unwrap t.
     *
     * @return the t
     */
    T unwrap();
}
