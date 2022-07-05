package com.livk.mapstruct.support;

import com.livk.mapstruct.converter.Converter;
import com.livk.mapstruct.converter.MapstructRegistry;

/**
 * <p>
 * Generic
 * </p>
 *
 * @author livk
 * @date 2022/6/9
 */
public class GenericMapstructService extends AbstractMapstructService implements MapstructRegistry {

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
