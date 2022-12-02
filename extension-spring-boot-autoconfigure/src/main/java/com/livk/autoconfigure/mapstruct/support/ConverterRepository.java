package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import org.springframework.core.ResolvableType;

/**
 * <p>
 * AbstractFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@SuppressWarnings("rawtypes")
public interface ConverterRepository {

    boolean contains(Class<?> sourceType, Class<?> targetType);

    Converter get(Class<?> sourceType, Class<?> targetType);

    default void put(Converter<?, ?> converter) {
        ResolvableType resolvableType = ResolvableType.forClass(converter.getClass());
        Class<?> sourceType = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0).resolve();
        Class<?> targetType = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(1).resolve();
        this.put(sourceType, targetType, converter);
    }

    void put(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter);

}
