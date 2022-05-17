package com.livk.mapstruct.support;

import com.google.common.collect.Table;
import com.livk.mapstruct.commom.Converter;

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

	Converter getConverter(Class<?> sourceClass, Class<?> targetClass);

	Table<Class<?>, Class<?>, Converter> getConverterMap();

	void put(Converter t);

}
