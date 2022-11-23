package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * <p>
 * AbstractFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@SuppressWarnings("rawtypes")
public interface ConverterRepository {

    boolean contains(Class<?> sourceClass, Class<?> targetClass);

    Converter get(Class<?> sourceClass, Class<?> targetClass);

    Map<Class<?>, Map<Class<?>, Converter>> getConverterMap();

    default void put(Converter<?, ?> converter) {
        ResolvableType resolvableType = ResolvableType.forClass(converter.getClass());
        Class<?> source = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0).resolve();
        Class<?> target = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(1).resolve();
        Assert.notNull(source, "source not null");
        Assert.notNull(target, "target not null");
        this.put(source, target, converter);
    }

    void put(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter);

}
