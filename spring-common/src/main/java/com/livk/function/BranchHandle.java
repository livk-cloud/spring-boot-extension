package com.livk.function;

import java.util.function.Predicate;

/**
 * <p>
 * BranchHandle
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
@FunctionalInterface
public interface BranchHandle {

    static <T> BranchHandle isTrueOrFalse(Predicate<T> predicate) {
        return (trueHandle, falseHandle) -> {
            if (predicate.test(null))
                trueHandle.run();
            else
                falseHandle.run();
        };
    }

    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);

}
