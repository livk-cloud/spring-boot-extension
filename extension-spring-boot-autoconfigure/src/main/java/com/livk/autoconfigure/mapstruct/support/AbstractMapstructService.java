package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;
import com.livk.autoconfigure.mapstruct.converter.MapstructRegistry;
import com.livk.autoconfigure.mapstruct.converter.MapstructService;
import com.livk.autoconfigure.mapstruct.exception.ConverterNotFoundException;
import com.livk.autoconfigure.mapstruct.repository.MapstructLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * <p>
 * AbstractMapstructService
 * </p>
 *
 * @author livk
 */
public abstract class AbstractMapstructService implements MapstructService, MapstructRegistry, ApplicationContextAware {

    /**
     * The Application context.
     */
    private List<MapstructLocator> mapstructLocators;

    @SuppressWarnings("unchecked")
    @Override
    public <S, T> T convert(S source, Class<T> targetType) {
        Class<S> sourceType = (Class<S>) source.getClass();
        for (MapstructLocator mapstructLocator : mapstructLocators) {
            Converter<S, T> sourceConverter = this.handler(sourceType, targetType, mapstructLocator);
            if (sourceConverter != null) {
                return sourceConverter.getTarget(source);
            }

            Converter<T, S> targetConverter = this.handler(targetType, sourceType, mapstructLocator);
            if (targetConverter != null) {
                return targetConverter.getSource(source);
            }
        }
        throw new ConverterNotFoundException(source + " to class " + targetType + " not found converter");
    }

    private <S, T> Converter<S, T> handler(Class<S> sourceType, Class<T> targetType, MapstructLocator mapstructLocator) {
        ConverterPair converterPair = ConverterPair.of(sourceType, targetType);
        Converter<S, T> converter = mapstructLocator.get(converterPair);
        if (converter != null) {
            this.addConverter(sourceType, targetType, converter);
        }
        return converter;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.mapstructLocators = applicationContext.getBeanProvider(MapstructLocator.class)
                .orderedStream()
                .toList();
    }
}
