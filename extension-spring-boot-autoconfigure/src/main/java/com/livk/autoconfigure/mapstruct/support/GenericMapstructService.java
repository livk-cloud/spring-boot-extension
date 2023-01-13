package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.MapstructRegistry;

/**
 * <p>
 * Generic
 * </p>
 *
 * @author livk
 */
public class GenericMapstructService extends AbstractMapstructService implements MapstructRegistry {

    /**
     * Instantiates a new Generic mapstruct service.
     *
     * @param converterRepository the converter repository
     */
    public GenericMapstructService(ConverterRepository converterRepository) {
        super(converterRepository);
    }

    @Override
    public void addConverter(Converter<?, ?> converter) {
        converterRepository.put(converter);
    }

    @Override
    public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType,
                                    Converter<? super S, ? extends T> converter) {
        converterRepository.put(sourceType, targetType, converter);
    }
}
