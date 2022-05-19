package com.livk.mapstruct.support;

import com.livk.mapstruct.exception.ConverterNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * MapstructService
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@RequiredArgsConstructor
public class MapstructService {

    private final ConverterRepository repository;

    public <T> T converter(Object source, Class<T> targetClass) {
        Class<?> sourceClass = source.getClass();
        if (repository.contains(sourceClass, targetClass)) {
            return (T) Objects.requireNonNull(repository.get(sourceClass, targetClass)).getTarget(source);
        } else if (repository.contains(targetClass, sourceClass)) {
            return (T) Objects.requireNonNull(repository.get(targetClass, sourceClass)).getSource(source);
        }
        throw new ConverterNotFoundException(source + " to class " + targetClass + " not found converter");
    }

    public <C extends Collection, T> Stream<T> converter(C sources, Class<T> targetClass) {
        Class<?> sourceClass = sources.stream().distinct().findFirst().orElseThrow().getClass();
        if (repository.contains(sourceClass, targetClass)) {
            return (Stream<T>) Objects.requireNonNull(repository.get(sourceClass, targetClass)).streamTarget(sources);
        } else if (repository.contains(targetClass, sourceClass)) {
            return (Stream<T>) Objects.requireNonNull(repository.get(targetClass, sourceClass)).streamSource(sources);
        }
        throw new ConverterNotFoundException(sources + " to class " + targetClass + " not found converter");
    }

}
