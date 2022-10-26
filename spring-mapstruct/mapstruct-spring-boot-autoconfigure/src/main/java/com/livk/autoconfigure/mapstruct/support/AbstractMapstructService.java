package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.MapstructService;
import com.livk.autoconfigure.mapstruct.exception.ConverterNotFoundException;
import lombok.RequiredArgsConstructor;

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
    public <S, T> T convert(S source, Class<T> targetClass) {
        Class<S> sourceClass = (Class<S>) source.getClass();
        if (converterRepository.contains(sourceClass, targetClass)) {
            return (T) converterRepository.get(sourceClass, targetClass).getTarget(source);
        } else if (converterRepository.contains(targetClass, sourceClass)) {
            return (T) converterRepository.get(targetClass, sourceClass).getSource(source);
        }
        throw new ConverterNotFoundException(source + " to class " + targetClass + " not found converter");
    }
}
