package com.livk.autoconfigure.mapstruct.repository;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;

/**
 * The interface Mapstruct locator.
 *
 * @author livk
 */
public interface MapstructLocator {

    /**
     * Get converter.
     *
     * @param <S>           the type parameter
     * @param <T>           the type parameter
     * @param converterPair the converter pair
     * @return the converter
     */
    <S, T> Converter<S, T> get(ConverterPair converterPair);
}
