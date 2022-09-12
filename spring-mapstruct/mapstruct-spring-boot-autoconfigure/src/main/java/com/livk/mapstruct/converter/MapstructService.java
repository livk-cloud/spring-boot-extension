package com.livk.mapstruct.converter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * MapstructService
 * </p>
 *
 * @author livk
 * @date 2022/6/9
 */
public interface MapstructService {

    <S, T> T converter(S source, Class<T> targetClass);

    default <S, T> Stream<T> converter(Collection<S> sources, Class<T> targetClass) {
        if (sources == null || sources.isEmpty()) {
            return Stream.empty();
        }
        return sources.stream().map(source -> converter(source, targetClass));
    }

    default <S, T> List<T> converterList(Collection<S> sources, Class<T> targetClass, boolean unmodifiable) {
        Stream<T> converter = converter(sources, targetClass);
        return unmodifiable ? converter.toList() : converter.collect(Collectors.toList());
    }

    default <S, T> List<T> converterList(Collection<S> sources, Class<T> targetClass) {
        return converterList(sources, targetClass, false);
    }

    default <S, T> List<T> converterUnmodifiableList(Collection<S> sources, Class<T> targetClass) {
        return converterList(sources, targetClass, true);
    }

    default <S, T> Set<T> converterSet(Collection<S> sources, Class<T> targetClass, boolean unmodifiable) {
        Stream<T> converter = converter(sources, targetClass);
        return unmodifiable ? converter.collect(Collectors.toUnmodifiableSet()) : converter.collect(Collectors.toSet());
    }

    default <S, T> Set<T> converterSet(Collection<S> sources, Class<T> targetClass) {
        return converterSet(sources, targetClass, false);
    }

    default <S, T> Set<T> converterUnmodifiableSet(Collection<S> sources, Class<T> targetClass) {
        return converterSet(sources, targetClass, true);
    }
}
