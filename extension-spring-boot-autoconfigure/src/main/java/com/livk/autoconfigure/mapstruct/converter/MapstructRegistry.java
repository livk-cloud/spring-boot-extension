package com.livk.autoconfigure.mapstruct.converter;

import com.livk.autoconfigure.mapstruct.support.ConverterSupport;

/**
 * <p>
 * MapstructFactory
 * </p>
 *
 * @author livk
 */
public interface MapstructRegistry {

    /**
     * Add converter.
     *
     * @param converterPair the converter pair
     * @param converter     the converter
     */
    void addConverter(ConverterPair converterPair, Converter<?, ?> converter);

    /**
     * Add converter.
     *
     * @param converter the converter
     */
    default void addConverter(Converter<?, ?> converter) {
        ConverterPair converterPair = ConverterSupport.parser(converter);
        if (converterPair != null) {
            this.addConverter(converterPair, converter);
        }
    }

    /**
     * Add converter.
     *
     * @param <S>        the type parameter
     * @param <T>        the type parameter
     * @param sourceType the source type
     * @param targetType the target type
     * @param converter  the converter
     */
    default <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
        ConverterPair converterPair = ConverterPair.of(sourceType, targetType);
        this.addConverter(converterPair, converter);
    }

}
