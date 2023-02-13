package com.livk.commons.collect.util;

import com.livk.commons.collect.EnumerationSpliterator;
import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>
 * StreamUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class StreamUtils {

    /**
     * Concat map.
     *
     * @param <K>  the type parameter
     * @param <V>  the type parameter
     * @param maps the maps
     * @return the map
     */
    @SafeVarargs
    public <K, V> Map<K, List<V>> concat(Map<K, V>... maps) {
        if (ObjectUtils.isEmpty(maps)) {
            return Collections.emptyMap();
        }
        return Arrays.stream(maps)
                .filter(Objects::nonNull)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue,
                                Collectors.toList())));
    }

    /**
     * Concat stream.
     *
     * @param <T> the type parameter
     * @param ts  the ts
     * @return the stream
     */
    @SafeVarargs
    public <T> Stream<T> concat(T[]... ts) {
        if (ObjectUtils.isEmpty(ts)) {
            return Stream.empty();
        }
        return Arrays.stream(ts).filter(Objects::nonNull).flatMap(Arrays::stream);
    }

    /**
     * Distinct predicate.
     *
     * @param <T>      the type parameter
     * @param function the function
     * @return the predicate
     */
    public <T> Predicate<T> distinct(Function<? super T, ?> function) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(function.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Convert stream.
     *
     * @param <T>      the type parameter
     * @param iterator the iterator
     * @return the stream
     */
    public <T> Stream<T> convert(Iterator<T> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
                false);
    }

    /**
     * Convert stream.
     *
     * @param <T>         the type parameter
     * @param enumeration the enumeration
     * @return the stream
     */
    public <T> Stream<T> convert(Enumeration<T> enumeration) {
        return StreamSupport.stream(
                EnumerationSpliterator.spliteratorUnknownSize(enumeration),
                false);
    }
}
