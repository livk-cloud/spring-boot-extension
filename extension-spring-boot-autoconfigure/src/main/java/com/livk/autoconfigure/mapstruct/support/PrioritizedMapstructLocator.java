package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;
import com.livk.autoconfigure.mapstruct.converter.MapstructRegistry;
import com.livk.autoconfigure.mapstruct.repository.ConverterRepository;
import com.livk.autoconfigure.mapstruct.repository.MapstructLocator;
import org.springframework.beans.factory.ObjectProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 */
class PrioritizedMapstructLocator implements MapstructLocator {

    private final MapstructRegistry mapstructRegistry;
    private final List<MapstructLocator> mapstructLocators = new ArrayList<>();
    private ConverterRepository converterRepository;

    public PrioritizedMapstructLocator(MapstructRegistry mapstructRegistry,
                                       ObjectProvider<MapstructLocator> mapstructLocatorObjectProvider) {
        this.mapstructRegistry = mapstructRegistry;
        for (MapstructLocator mapstructLocator : mapstructLocatorObjectProvider.orderedStream().toList()) {
            if (mapstructLocator instanceof ConverterRepository repository) {
                this.converterRepository = repository;
            } else {
                mapstructLocators.add(mapstructLocator);
            }
        }
    }

    @Override
    public <S, T> Converter<S, T> get(ConverterPair converterPair) {
        Converter<S, T> converter = converterRepository.get(converterPair);
        if (converter == null) {
            for (MapstructLocator mapstructLocator : mapstructLocators) {
                converter = mapstructLocator.get(converterPair);
                if (converter != null) {
                    mapstructRegistry.addConverter(converterPair, converter);
                    return converter;
                }
            }
        }
        return converter;
    }
}
