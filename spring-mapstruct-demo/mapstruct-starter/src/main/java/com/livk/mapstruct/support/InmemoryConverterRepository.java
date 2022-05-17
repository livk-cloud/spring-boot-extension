package com.livk.mapstruct.support;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.livk.mapstruct.commom.Converter;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

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

	private final Table<Class<?>, Class<?>, Converter> converterTable = HashBasedTable.create();

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
	public Table<Class<?>, Class<?>, Converter> getConverterMap() {
		return converterTable;
	}

	@Override
	public Converter getConverter(Class<?> sourceClass, Class<?> targetClass) {
		if (converterTable.contains(sourceClass, targetClass)) {
			return converterTable.get(sourceClass, targetClass);
		}
		else if (converterTable.contains(targetClass, sourceClass)) {
			return converterTable.get(targetClass, sourceClass);
		}
		return null;
	}

}
