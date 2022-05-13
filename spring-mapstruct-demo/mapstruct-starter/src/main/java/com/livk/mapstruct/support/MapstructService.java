package com.livk.mapstruct.support;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.livk.mapstruct.commom.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * MapstructService
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequiredArgsConstructor
public class MapstructService implements ConverterRepository<Converter> {

	private final Table<Class<?>, Class<?>, Converter> converterTable = HashBasedTable.create();

	public <T> T converter(Object source, Class<T> targetClass) {
		Class<?> sourceClass = source.getClass();
		if (converterTable.contains(sourceClass, targetClass)) {
			return (T) Objects.requireNonNull(converterTable.get(sourceClass, targetClass)).getTarget(source);
		}
		else if (converterTable.contains(targetClass, sourceClass)) {
			return (T) Objects.requireNonNull(converterTable.get(targetClass, sourceClass)).getSource(source);
		}
		return BeanUtils.instantiateClass(targetClass);
	}

	public <C extends Collection, T> Stream<T> converter(C sources, Class<T> targetClass) {
		Class<?> sourceClass = sources.stream().distinct().findFirst().orElseThrow().getClass();
		if (converterTable.contains(sourceClass, targetClass)) {
			return (Stream<T>) Objects.requireNonNull(converterTable.get(sourceClass, targetClass))
					.streamTarget(sources);
		}
		else if (converterTable.contains(targetClass, sourceClass)) {
			return (Stream<T>) Objects.requireNonNull(converterTable.get(targetClass, sourceClass))
					.streamSource(sources);
		}
		return Stream.empty();
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
