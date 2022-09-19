package com.livk.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

    public <K, V> Map<K, List<V>> concat(Map<K, V> m1, Map<K, V> m2) {
        return Stream.concat(m1.entrySet().stream(), m2.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        kListEntry -> kListEntry.getValue()
                                .stream()
                                .map(Map.Entry::getValue)
                                .collect(Collectors.toList())));
    }

    public <T> Predicate<T> distinct(Function<? super T, ?> function) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(function.apply(t), Boolean.TRUE) == null;
    }
}
