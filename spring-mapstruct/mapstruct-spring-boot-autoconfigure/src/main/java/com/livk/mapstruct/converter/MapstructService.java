package com.livk.mapstruct.converter;

import com.livk.mapstruct.support.ConverterRepository;

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

    <S, T> T converter(S source, Class<T> targetClass);

    <S, T> Stream<T> converter(Collection<S> sources, Class<T> targetClass);

    ConverterRepository getConverterRepository();

}
