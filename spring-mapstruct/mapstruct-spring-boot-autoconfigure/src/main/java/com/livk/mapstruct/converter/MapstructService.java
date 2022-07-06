package com.livk.mapstruct.converter;

import com.livk.mapstruct.support.ConverterRepository;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * <p>
 * MapstructService
 * </p>
 *
 * @author livk
 * @date 2022/6/9
 */
public interface MapstructService {

    static void addBeans(MapstructRegistry registry, ListableBeanFactory beanFactory) {
        beanFactory.getBeansOfType(Converter.class)
                .values()
                .forEach(registry::addConverter);
    }

    <T> T converter(Object source, Class<T> targetClass);

    <T> Stream<T> converter(Collection<?> sources, Class<T> targetClass);

    ConverterRepository getConverterRepository();

}
