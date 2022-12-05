package com.livk.autoconfigure.mapstruct.converter;

/**
 * <p>
 * MapstructFactory
 * </p>
 *
 * @author livk
 *
 */
public interface MapstructRegistry {

    void addConverter(Converter<?, ?> converter);

    <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter);

}
