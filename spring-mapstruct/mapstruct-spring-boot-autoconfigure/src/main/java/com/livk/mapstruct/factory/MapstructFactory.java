package com.livk.mapstruct.factory;

import com.livk.mapstruct.converter.Converter;
import com.livk.mapstruct.converter.MapstructRegistry;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * <p>
 * MapstructFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
public class MapstructFactory {
    public MapstructFactory(MapstructRegistry registry, ListableBeanFactory beanFactory) {
        addBeans(registry, beanFactory);
    }

    public static void addBeans(MapstructRegistry registry, ListableBeanFactory beanFactory) {
        beanFactory.getBeansOfType(Converter.class).values().forEach(registry::addConverter);
    }
}
