package com.livk.mapstruct.support;

import com.livk.mapstruct.converter.MapstructService;
import com.livk.mapstruct.exception.ConverterNotFoundException;
import lombok.Getter;
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
    @Getter(onMethod_ = @Override)
    protected final ConverterRepository converterRepository;

    @Override
    public <T> T converter(Object source, Class<T> targetClass) {
        Class<?> sourceClass = source.getClass();
        if (converterRepository.contains(sourceClass, targetClass)) {
            return (T) converterRepository.get(sourceClass, targetClass).getTarget(source);
        } else if (converterRepository.contains(targetClass, sourceClass)) {
            return (T) converterRepository.get(targetClass, sourceClass).getSource(source);
        }
        throw new ConverterNotFoundException(source + " to class " + targetClass + " not found converter");
    }

    @Override
    public <T> Stream<T> converter(Collection<?> sources, Class<T> targetClass) {
        Class<?> sourceClass = sources.stream().distinct().findFirst().orElseThrow().getClass();
        if (converterRepository.contains(sourceClass, targetClass)) {
            return (Stream<T>) converterRepository.get(sourceClass, targetClass).streamTarget(sources);
        } else if (converterRepository.contains(targetClass, sourceClass)) {
            return (Stream<T>) converterRepository.get(targetClass, sourceClass).streamSource(sources);
        }
        throw new ConverterNotFoundException(sources + " to class " + targetClass + " not found converter");
    }
}
