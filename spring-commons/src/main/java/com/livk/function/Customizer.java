package com.livk.function;

/**
 * <p>
 * Customizer
 * </p>
 *
 * @author livk
 * @date 2022/10/18
 */
@FunctionalInterface
public interface Customizer<T> {

    void customize(T t);
}
