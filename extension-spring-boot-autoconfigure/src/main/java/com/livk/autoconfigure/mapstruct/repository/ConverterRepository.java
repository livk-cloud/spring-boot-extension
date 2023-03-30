package com.livk.autoconfigure.mapstruct.repository;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;

/**
 * <p>
 * AbstractFactory
 * </p>
 *
 * @author livk
 */
public interface ConverterRepository extends MapstructLocator {

    /**
     * Contains boolean.
     *
     * @param sourceType the source type
     * @param targetType the target type
     * @return the boolean
     */
    default boolean contains(Class<?> sourceType, Class<?> targetType) {
        return this.contains(ConverterPair.of(sourceType, targetType));
    }

    /**
     * Contains boolean.
     *
     * @param converterPair the converter pair
     * @return the boolean
     */
    boolean contains(ConverterPair converterPair);

    /**
     * Put.
     *
     * @param converterPair the converter pair
     * @param converter     the converter
     */
    void put(ConverterPair converterPair, Converter<?, ?> converter);

    /**
     * Compute if absent.
     *
     * @param converterPair the converter pair
     * @param converter     the converter
     */
    Converter<?, ?> computeIfAbsent(ConverterPair converterPair, Converter<?, ?> converter);

}
