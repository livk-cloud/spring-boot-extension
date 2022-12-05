package com.livk.commons.function;

/**
 * <p>
 * Customizer
 * </p>
 *
 * @author livk
 */
@FunctionalInterface
public interface Customizer<T> {

    void customize(T t);
}
