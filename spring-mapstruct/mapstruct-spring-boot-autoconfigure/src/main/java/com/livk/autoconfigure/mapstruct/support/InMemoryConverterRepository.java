package com.livk.autoconfigure.mapstruct.support;

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
 * @date 2022/5/18
 */
@SuppressWarnings("rawtypes")
public class InMemoryConverterRepository implements ConverterRepository {

    // 本质是HashMap<Class,HashMap<Class,Converter>> put添加synchronized锁
    private final Map<ConverterPair, Converter> converterMap = new ConcurrentHashMap<>();

    @Override
    public boolean contains(Class<?> sourceType, Class<?> targetType) {
        return converterMap.containsKey(ConverterPair.of(sourceType, targetType));
    }

    @Override
    public void put(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter) {
        converterMap.put(ConverterPair.of(sourceType, targetType), converter);
    }

    @Override
    public Converter get(Class<?> sourceType, Class<?> targetType) {
        return converterMap.get(ConverterPair.of(sourceType, targetType));
    }

}
