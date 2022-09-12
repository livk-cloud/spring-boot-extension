package com.livk.mapstruct.support;

import com.livk.mapstruct.converter.MapstructService;
import com.livk.mapstruct.exception.ConverterNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * <p>
 * AbstractMapstructService
 * </p>
 *
 * @author livk
 * @date 2022/6/20
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public abstract class AbstractMapstructService implements MapstructService {
    protected final ConverterRepository converterRepository;

    @Override
    public <S, T> T converter(S source, Class<T> targetClass) {
        Class<S> sourceClass = (Class<S>) source.getClass();
        if (converterRepository.contains(sourceClass, targetClass)) {
            return (T) converterRepository.get(sourceClass, targetClass).getTarget(source);
        } else if (converterRepository.contains(targetClass, sourceClass)) {
            return (T) converterRepository.get(targetClass, sourceClass).getSource(source);
        }
        throw new ConverterNotFoundException(source + " to class " + targetClass + " not found converter");
    }

    @Override
    public <S, T> Stream<T> converter(Collection<S> sources, Class<T> targetClass) {
        if (sources == null || sources.isEmpty()) {
            return Stream.empty();
        }
        return sources.stream().map(source -> converter(source, targetClass));
    }
}
