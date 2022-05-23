package com.livk.mapstruct.support;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.livk.common.Pair;
import com.livk.mapstruct.commom.Converter;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * InmemoryConverterRepository
 * </p>
 *
 * @author livk
 * @date 2022/5/18
 */
@SuppressWarnings("rawtypes")
public class InmemoryConverterRepository implements ConverterRepository {
    //本质是HashMap<Class,HashMap<Class,Converter>> put添加synchronized锁
    private final Table<Class<?>, Class<?>, Converter> converterTable = HashBasedTable.create();

    @Override
    public boolean contains(Class<?> sourceClass, Class<?> targetClass) {
        return converterTable.contains(sourceClass, targetClass);
    }

    @Override
    public synchronized void put(Converter converter) {
        ResolvableType resolvableType = ResolvableType.forClass(converter.getClass());
        Class<?> source = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0).resolve();
        Class<?> target = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(1).resolve();
        Assert.notNull(source, "source not null");
        Assert.notNull(target, "target not null");
        converterTable.put(source, target, converter);
    }

    @Override
    public Map<Pair<Class<?>, Class<?>>, Converter> getConverterMap() {
        Map<Pair<Class<?>, Class<?>>, Converter> converterMap = new HashMap<>();
        Set<Class<?>> classes = converterTable.rowKeySet();
        for (Class<?> aClass : classes) {
            Map<Class<?>, Converter> row = converterTable.row(aClass);
            row.forEach((k, v) -> converterMap.put(Pair.of(aClass, k), v));
        }
        return converterMap;
    }

    @Override
    public Converter get(Class<?> sourceClass, Class<?> targetClass) {
        return converterTable.get(sourceClass, targetClass);
    }

}
