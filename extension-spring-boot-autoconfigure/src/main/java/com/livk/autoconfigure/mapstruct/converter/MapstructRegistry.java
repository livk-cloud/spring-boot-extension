package com.livk.autoconfigure.mapstruct.converter;

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
     * @param converter the converter
     */
    void addConverter(Converter<?, ?> converter);

    /**
     * Add converter.
     *
     * @param <S>        the type parameter
     * @param <T>        the type parameter
     * @param sourceType the source type
     * @param targetType the target type
     * @param converter  the converter
     */
    <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter);

}
