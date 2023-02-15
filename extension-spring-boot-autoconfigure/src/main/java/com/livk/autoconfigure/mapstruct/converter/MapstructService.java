package com.livk.autoconfigure.mapstruct.converter;

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
 */
public interface MapstructService {

    /**
     * Convert t.
     *
     * @param <S>        the type parameter
     * @param <T>        the type parameter
     * @param source     the source
     * @param targetType the target class
     * @return the t
     */
    <S, T> T convert(S source, Class<T> targetType);

    /**
     * Convert stream.
     *
     * @param <S>        the type parameter
     * @param <T>        the type parameter
     * @param sources    the sources
     * @param targetType the target class
     * @return the stream
     */
    default <S, T> Stream<T> convert(Collection<S> sources, Class<T> targetType) {
        if (sources == null || sources.isEmpty()) {
            return Stream.empty();
        }
        return sources.stream().map(source -> convert(source, targetType));
    }

    /**
     * Convert list.
     *
     * @param <S>        the type parameter
     * @param <T>        the type parameter
     * @param sources    the sources
     * @param targetType the target class
     * @return the list
     */
    default <S, T> List<T> convertList(Collection<S> sources, Class<T> targetType) {
        return convert(sources, targetType).collect(Collectors.toList());
    }

    /**
     * Convert unmodifiable list.
     *
     * @param <S>        the type parameter
     * @param <T>        the type parameter
     * @param sources    the sources
     * @param targetType the target class
     * @return the list
     */
    default <S, T> List<T> convertUnmodifiableList(Collection<S> sources, Class<T> targetType) {
        return convert(sources, targetType).toList();
    }

    /**
     * Convert set.
     *
     * @param <S>        the type parameter
     * @param <T>        the type parameter
     * @param sources    the sources
     * @param targetType the target class
     * @return the set
     */
    default <S, T> Set<T> convertSet(Collection<S> sources, Class<T> targetType) {
        return convert(sources, targetType).collect(Collectors.toSet());
    }

    /**
     * Convert unmodifiable set.
     *
     * @param <S>        the type parameter
     * @param <T>        the type parameter
     * @param sources    the sources
     * @param targetType the target class
     * @return the set
     */
    default <S, T> Set<T> convertUnmodifiableSet(Collection<S> sources, Class<T> targetType) {
        return convert(sources, targetType).collect(Collectors.toUnmodifiableSet());
    }
}
