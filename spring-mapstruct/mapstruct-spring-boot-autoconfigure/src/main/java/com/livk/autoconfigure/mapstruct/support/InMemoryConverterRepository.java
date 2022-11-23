package com.livk.autoconfigure.mapstruct.support;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.livk.autoconfigure.mapstruct.converter.Converter;

import java.util.Map;

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
    private final Table<Class<?>, Class<?>, Converter> converterTable = HashBasedTable.create();

    @Override
    public boolean contains(Class<?> sourceClass, Class<?> targetClass) {
        return converterTable.contains(sourceClass, targetClass);
    }

    @Override
    public void put(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter) {
        converterTable.put(sourceType, targetType, converter);
    }

    /**
     * 加载时间比较晚，InitializingBean->@PostConstruct->加载
     *
     * @return map
     */
    @Override
    public Map<Class<?>, Map<Class<?>, Converter>> getConverterMap() {
        return converterTable.rowMap();
    }

    @Override
    public Converter get(Class<?> sourceClass, Class<?> targetClass) {
        return converterTable.get(sourceClass, targetClass);
    }

}
