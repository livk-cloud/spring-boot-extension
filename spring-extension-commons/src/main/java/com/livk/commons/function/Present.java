package com.livk.commons.function;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>
 * PresentOrElseHandler
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
@FunctionalInterface
public interface Present<T> {

    /**
     * Handler present.
     *
     * @param <T>       the type parameter
     * @param t         the t
     * @param predicate the predicate
     * @return the present
     */
    static <T> Present<T> handler(T t, Predicate<T> predicate) {
        return (action, emptyAction) -> {
            if (predicate.test(t))
                action.accept(t);
            else
                emptyAction.run();
        };
    }

    /**
     * Present.
     *
     * @param action      the action
     * @param emptyAction the empty action
     */
    void present(Consumer<T> action, Runnable emptyAction);

}
