package com.livk.commons.bean;

/**
 * The interface Wrapper.
 */
public interface Wrapper {

    /**
     * Try unwrap t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    static <T> T tryUnwrap(Object obj, Class<T> type) {
        if (obj instanceof Wrapper wrapper) {
            if (wrapper.isWrapperFor(type)) {
                return wrapper.unwrap(type);
            }
        }
        return type.cast(obj);
    }

    /**
     * Of wrapper.
     *
     * @param <T>   the type parameter
     * @param value the value
     * @return the wrapper
     */
    static <T> Wrapper of(T value) {
        return new GenericWrapper.SimpleWrapper<>(value);
    }

    /**
     * Unwrap t.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the t
     */
    <T> T unwrap(Class<T> type);

    /**
     * Is wrapper for boolean.
     *
     * @param type the type
     * @return the boolean
     */
    boolean isWrapperFor(Class<?> type);
}
