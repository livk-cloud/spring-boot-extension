package com.livk.commons.util;

import lombok.experimental.UtilityClass;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <p>
 * StringUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class ObjectUtils extends org.springframework.util.ObjectUtils {

    /**
     * All checked boolean.
     *
     * @param <T>       the type parameter
     * @param predicate the predicate
     * @param ts        the ts
     * @return the boolean
     */
    @SafeVarargs
    public <T> boolean allChecked(Predicate<T> predicate, T... ts) {
        return !ObjectUtils.isEmpty(ts) && Stream.of(ts).allMatch(predicate);
    }

    /**
     * Any checked boolean.
     *
     * @param <T>       the type parameter
     * @param predicate the predicate
     * @param ts        the ts
     * @return the boolean
     */
    @SafeVarargs
    public <T> boolean anyChecked(Predicate<T> predicate, T... ts) {
        return !ObjectUtils.isEmpty(ts) && Stream.of(ts).anyMatch(predicate);
    }

}
