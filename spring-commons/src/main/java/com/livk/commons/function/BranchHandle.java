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

    /**
     * Is true or false branch handle.
     *
     * @param <T>       the type parameter
     * @param obj       the obj
     * @param predicate the predicate
     * @return the branch handle
     */
    static <T> BranchHandle isTrueOrFalse(T obj, Predicate<T> predicate) {
        return (trueHandle, falseHandle) -> {
            if (predicate.test(obj))
                trueHandle.run();
            else
                falseHandle.run();
        };
    }

    /**
     * True or false handle.
     *
     * @param trueHandle  the true handle
     * @param falseHandle the false handle
     */
    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);

}
