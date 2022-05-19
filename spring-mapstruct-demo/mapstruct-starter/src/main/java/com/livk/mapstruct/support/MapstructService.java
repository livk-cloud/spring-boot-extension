package com.livk.mapstruct.support;

import com.google.common.collect.Table;
import com.livk.mapstruct.commom.Converter;
import org.springframework.beans.BeanUtils;

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
public class MapstructService {

	private final Table<Class<?>, Class<?>, Converter> converterTable;

	public MapstructService(ConverterRepository repository) {
		converterTable = repository.getConverterMap();
	}

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

}
