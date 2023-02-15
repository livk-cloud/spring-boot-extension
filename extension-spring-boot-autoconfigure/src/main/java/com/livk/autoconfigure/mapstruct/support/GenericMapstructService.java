package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;
import com.livk.autoconfigure.mapstruct.converter.MapstructRegistry;
import com.livk.autoconfigure.mapstruct.repository.ConverterRepository;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Generic
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class GenericMapstructService extends AbstractMapstructService implements MapstructRegistry {

    private final ConverterRepository converterRepository;

    @Override
    public void addConverter(Converter<?, ?> converter) {
        ConverterPair converterPair = ConverterSupport.parser(converter);
        converterRepository.computeIfAbsent(converterPair, converter);
    }

    @Override
    public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType,
                                    Converter<? super S, ? extends T> converter) {
        ConverterPair converterPair = ConverterPair.of(sourceType, targetType);
        converterRepository.computeIfAbsent(converterPair, converter);
    }
}
