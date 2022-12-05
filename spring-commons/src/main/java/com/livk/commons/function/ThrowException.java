package com.livk.commons.function;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>
 * ThrowException
 * </p>
 *
 * @author livk
 */
@FunctionalInterface
public interface ThrowException {

    /**
     * Is true throw exception.
     *
     * @param <T>       the type parameter
     * @param predicate the b
     * @return the throw exception
     */
    static <T> ThrowException isTrue(T obj, Predicate<T> predicate) {
        return (t) -> {
            if (predicate.test(obj))
                throw t;
        };
    }

    /**
     * Throw exception.
     *
     * @param t the message
     * @throws Throwable the throwable
     */
    void throwException(Throwable t) throws Throwable;

    default void throwException(Supplier<Throwable> supplier) throws Throwable {
        Objects.requireNonNull(supplier);
        throwException(supplier.get());
    }

}
