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
    public void addConverter(ConverterPair converterPair, Converter<?, ?> converter) {
        converterRepository.computeIfAbsent(converterPair, converter);
    }
}
