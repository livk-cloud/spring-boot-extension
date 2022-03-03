package com.livk.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * <p>
 * StreamUtils
 * </p>
 *
 * @author livk
 * @date 2022/3/4
 */
@UtilityClass
public class StreamUtils {
    @SuppressWarnings("unchecked")
    public <T> T[] concat(T[] start, T[] end, Class<T> clazz) {
        return Stream.concat(Stream.of(start), Stream.of(end))
                .toArray(value -> (T[]) Array.newInstance(clazz, value));
    }

    public <T> Stream<T> concat(Collection<T> start, Collection<T> end) {
        return Stream.concat(Stream.of(start), Stream.of(end))
                .flatMap(Collection::stream);
    }
}
