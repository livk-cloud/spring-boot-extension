package com.livk.mapstruct.support;

import com.livk.mapstruct.commom.Converter;

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

	void put(Converter t);

}
