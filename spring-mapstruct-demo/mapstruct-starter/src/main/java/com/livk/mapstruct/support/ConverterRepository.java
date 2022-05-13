package com.livk.mapstruct.support;

import com.google.common.collect.Table;

/**
 * <p>
 * AbstractFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
public interface ConverterRepository<T> {

	T getConverter(Class<?> sourceClass, Class<?> targetClass);

	Table<Class<?>, Class<?>, T> getConverterMap();

	void put(T t);

}
