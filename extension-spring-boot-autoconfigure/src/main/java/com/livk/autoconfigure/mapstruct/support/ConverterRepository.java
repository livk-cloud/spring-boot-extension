package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import org.springframework.core.ResolvableType;

/**
 * <p>
 * AbstractFactory
 * </p>
 *
 * @author livk
 */
@SuppressWarnings("rawtypes")
public interface ConverterRepository {

    /**
     * Contains boolean.
     *
     * @param sourceType the source type
     * @param targetType the target type
     * @return the boolean
     */
    boolean contains(Class<?> sourceType, Class<?> targetType);

    /**
     * Get converter.
     *
     * @param sourceType the source type
     * @param targetType the target type
     * @return the converter
     */
    Converter get(Class<?> sourceType, Class<?> targetType);

    /**
     * Put.
     *
     * @param converter the converter
     */
    default void put(Converter<?, ?> converter) {
        ResolvableType resolvableType = ResolvableType.forClass(converter.getClass());
        Class<?> sourceType = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0).resolve();
        Class<?> targetType = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(1).resolve();
        this.put(sourceType, targetType, converter);
    }

    /**
     * Put.
     *
     * @param sourceType the source type
     * @param targetType the target type
     * @param converter  the converter
     */
    void put(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter);

}
