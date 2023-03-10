package com.livk.commons.bean;

import com.livk.commons.proxy.ProxyType;

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
     * Proxy wrapper.
     *
     * @param <T>  the type parameter
     * @param t    the t
     * @param type the type
     * @return the wrapper
     */
    static <T> Wrapper<T> proxy(T t, ProxyType type) {
        WrapperProxy<T> proxy = new WrapperProxy<>(t);
        return switch (type) {
            case JDK_PROXY -> proxy.unwrap();
            case BYTE_BUDDY -> new ByteBuddyWrapperProxy<>(proxy).unwrap();
        };
    }

    /**
     * Unwrap t.
     *
     * @return the t
     */
    T unwrap();
}
