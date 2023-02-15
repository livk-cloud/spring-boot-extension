package com.livk.autoconfigure.mapstruct.repository;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * The type Spring mapstruct locator.
 *
 * @author livk
 */
public class SpringMapstructLocator implements MapstructLocator, ApplicationContextAware {

    /**
     * The Application context.
     */
    private ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    @Override
    public <S, T> Converter<S, T> get(ConverterPair converterPair) {
        Class<?> sourceType = converterPair.getSourceType();
        Class<?> targetType = converterPair.getTargetType();
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Converter.class, sourceType, targetType);
        return (Converter<S, T>) applicationContext.getBeanProvider(resolvableType).getIfUnique();
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
