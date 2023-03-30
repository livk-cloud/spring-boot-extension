package com.livk.autoconfigure.mapstruct.repository;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * InMemoryConverterRepository
 * </p>
 *
 * @author livk
 */
public class InMemoryConverterRepository implements ConverterRepository {

    private final Map<ConverterPair, Converter<?, ?>> converterMap = new ConcurrentHashMap<>();

    @Override
    public boolean contains(ConverterPair converterPair) {
        return converterMap.containsKey(converterPair);
    }

    @Override
    public void put(ConverterPair converterPair, Converter<?, ?> converter) {
        converterMap.put(converterPair, converter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S, T> Converter<S, T> get(ConverterPair converterPair) {
        return (Converter<S, T>) converterMap.get(converterPair);
    }

    @Override
    public Converter<?, ?> computeIfAbsent(ConverterPair converterPair, Converter<?, ?> converter) {
        return converterMap.computeIfAbsent(converterPair, pair -> converter);
    }
}
