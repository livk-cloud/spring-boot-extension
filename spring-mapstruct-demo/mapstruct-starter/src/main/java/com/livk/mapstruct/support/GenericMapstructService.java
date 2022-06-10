package com.livk.mapstruct.support;

import com.livk.mapstruct.converter.Converter;
import com.livk.mapstruct.converter.MapstructRegistry;
import com.livk.mapstruct.converter.MapstructService;
import com.livk.mapstruct.exception.ConverterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * Generic
 * </p>
 *
 * @author livk
 * @date 2022/6/9
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class GenericMapstructService implements MapstructService, MapstructRegistry {

	private final ConverterRepository repository;

	@Override
	public void addConverter(Converter<?, ?> converter) {
		repository.put(converter);
	}

	@Override
	public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType,
			Converter<? super S, ? extends T> converter) {
		repository.put(sourceType, targetType, converter);
	}

	@Override
	public <T> T converter(Object source, Class<T> targetClass) {
		Class<?> sourceClass = source.getClass();
		if (repository.contains(sourceClass, targetClass)) {
			return (T) Objects.requireNonNull(repository.get(sourceClass, targetClass)).getTarget(source);
		}
		else if (repository.contains(targetClass, sourceClass)) {
			return (T) Objects.requireNonNull(repository.get(targetClass, sourceClass)).getSource(source);
		}
		throw new ConverterNotFoundException(source + " to class " + targetClass + " not found converter");
	}

	@Override
	public <T> Stream<T> converter(Collection<?> sources, Class<T> targetClass) {
		Class<?> sourceClass = sources.stream().distinct().findFirst().orElseThrow().getClass();
		if (repository.contains(sourceClass, targetClass)) {
			return (Stream<T>) Objects.requireNonNull(repository.get(sourceClass, targetClass)).streamTarget(sources);
		}
		else if (repository.contains(targetClass, sourceClass)) {
			return (Stream<T>) Objects.requireNonNull(repository.get(targetClass, sourceClass)).streamSource(sources);
		}
		throw new ConverterNotFoundException(sources + " to class " + targetClass + " not found converter");
	}

	@Override
	public ConverterRepository getConverterRepository() {
		return this.repository;
	}

	public static void addBeans(MapstructRegistry registry, ListableBeanFactory beanFactory) {
		beanFactory.getBeansOfType(Converter.class).values().forEach(registry::addConverter);
	}

}
