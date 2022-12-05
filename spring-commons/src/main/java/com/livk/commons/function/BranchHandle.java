package com.livk.commons.function;

import java.util.function.Predicate;

/**
 * <p>
 * BranchHandle
 * </p>
 *
 * @author livk
 */
@FunctionalInterface
public interface BranchHandle {

    static <T> BranchHandle isTrueOrFalse(T obj, Predicate<T> predicate) {
        return (trueHandle, falseHandle) -> {
            if (predicate.test(obj))
                trueHandle.run();
            else
                falseHandle.run();
        };
    }

    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);

}
