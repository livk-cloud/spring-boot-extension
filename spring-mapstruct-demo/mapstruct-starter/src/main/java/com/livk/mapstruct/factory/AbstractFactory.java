package com.livk.mapstruct.factory;

import com.google.common.collect.Table;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * <p>
 * AbstractFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
public interface AbstractFactory<T> extends BeanPostProcessor {

	T getConverter(Class<?> sourceClass, Class<?> targetClass);

	Table<Class<?>, Class<?>, T> getConverterMap();

	void put(T t);

}
