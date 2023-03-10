package com.livk.commons.function;

/**
 * <p>
 * Customizer
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
@FunctionalInterface
public interface Customizer<T> {

    /**
     * Customize.
     *
     * @param t the t
     */
    void customize(T t);
}
