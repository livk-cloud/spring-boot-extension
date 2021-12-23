package com.livk.function;

import java.util.function.Predicate;

/**
 * <p>
 * ThrowException
 * </p>
 *
 * @author livk
 * @date 2021 /11/26
 */
@FunctionalInterface
public interface ThrowException {

    /**
     * Throw exception.
     *
     * @param t the message
     * @throws Throwable the throwable
     */
    void throwException(Throwable t) throws Throwable;

    /**
     * Is true throw exception.
     *
     * @param <T>       the type parameter
     * @param predicate the b
     * @return the throw exception
     */
    static <T> ThrowException isTrue(Predicate<T> predicate) {
        return (t) -> {
            if (predicate.test(null)) throw t;
        };
    }
}
