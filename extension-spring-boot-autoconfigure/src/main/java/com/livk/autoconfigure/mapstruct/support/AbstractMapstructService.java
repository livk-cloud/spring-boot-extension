package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;
import com.livk.autoconfigure.mapstruct.converter.MapstructRegistry;
import com.livk.autoconfigure.mapstruct.converter.MapstructService;
import com.livk.autoconfigure.mapstruct.exception.ConverterNotFoundException;
import com.livk.autoconfigure.mapstruct.repository.MapstructLocator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
    private PrioritizedMapstructLocator mapstructLocator;

    @SuppressWarnings("unchecked")
    @Override
    public <S, T> T convert(S source, Class<T> targetType) {
        Class<S> sourceType = (Class<S>) source.getClass();
        Converter<S, T> sourceConverter = this.handler(sourceType, targetType);
        if (sourceConverter != null) {
            return sourceConverter.getTarget(source);
        }

        Converter<T, S> targetConverter = this.handler(targetType, sourceType);
        if (targetConverter != null) {
            return targetConverter.getSource(source);
        }
        throw new ConverterNotFoundException(source + " to class " + targetType + " not found converter");
    }

    private <S, T> Converter<S, T> handler(Class<S> sourceType, Class<T> targetType) {
        ConverterPair converterPair = ConverterPair.of(sourceType, targetType);
        return mapstructLocator.get(converterPair);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ObjectProvider<MapstructLocator> mapstructLocators = applicationContext.getBeanProvider(MapstructLocator.class);
        this.mapstructLocator = new PrioritizedMapstructLocator(this, mapstructLocators);
    }
}
