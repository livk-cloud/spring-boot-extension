package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.MapstructService;
import com.livk.autoconfigure.mapstruct.exception.ConverterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;

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
public abstract class AbstractMapstructService implements MapstructService, ApplicationContextAware {
    protected final ConverterRepository converterRepository;

    protected ApplicationContext applicationContext;

    @Override
    public <S, T> T convert(S source, Class<T> targetClass) {
        Class<S> sourceClass = (Class<S>) source.getClass();
        if (converterRepository.contains(sourceClass, targetClass)) {
            return (T) converterRepository.get(sourceClass, targetClass).getTarget(source);
        } else if (converterRepository.contains(targetClass, sourceClass)) {
            return (T) converterRepository.get(targetClass, sourceClass).getSource(source);
        } else {
            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Converter.class, sourceClass, targetClass);
            Converter<S, T> sourceConverter = (Converter<S, T>) applicationContext.getBeanProvider(resolvableType).getIfUnique();
            if (sourceConverter != null) {
                converterRepository.put(sourceClass, targetClass, sourceConverter);
                return sourceConverter.getTarget(source);
            }
            resolvableType = ResolvableType.forClassWithGenerics(Converter.class, targetClass, sourceClass);
            Converter<T, S> targetConverter = (Converter<T, S>) applicationContext.getBeanProvider(resolvableType).getIfUnique();
            if (targetConverter != null) {
                converterRepository.put(targetClass, sourceClass, targetConverter);
                return targetConverter.getSource(source);
            }
        }
        throw new ConverterNotFoundException(source + " to class " + targetClass + " not found converter");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
