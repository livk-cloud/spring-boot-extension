/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.collect.util;

import com.livk.commons.collect.EnumerationSpliterator;
import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.*;

/**
 * <p>
 * StreamUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class StreamUtils {

	/**
	 * Concat map.
	 *
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @param maps the maps
	 * @return the map
	 */
	@SafeVarargs
	public <K, V> Map<K, List<V>> concat(Map<K, V>... maps) {
		if (ObjectUtils.isEmpty(maps)) {
			return Collections.emptyMap();
		}
		return Arrays.stream(maps)
			.filter(Objects::nonNull)
			.flatMap(map -> map.entrySet().stream())
			.collect(Collectors.groupingBy(Map.Entry::getKey,
				Collectors.mapping(Map.Entry::getValue,
					Collectors.toList())));
	}

	/**
	 * Concat stream.
	 *
	 * @param <T> the type parameter
	 * @param ts  the ts
	 * @return the stream
	 */
	@SafeVarargs
	public <T> Stream<T> concat(T[]... ts) {
		if (ObjectUtils.isEmpty(ts)) {
			return Stream.empty();
		}
		return Arrays.stream(ts)
			.filter(Objects::nonNull)
			.flatMap(Arrays::stream);
	}

	/**
	 * Concat string [ ].
	 *
	 * @param distinct 是否去重
	 * @param strArr   str[]
	 * @return the string [ ]
	 */
	public String[] concat(boolean distinct, String[]... strArr) {
		Stream<String> concat = concat(strArr);
		if (distinct) {
			concat = concat.distinct();
		}
		return concat.toArray(String[]::new);
	}

	/**
	 * Concat distinct string [ ].
	 *
	 * @param strArr str[]
	 * @return the string [ ]
	 */
	public String[] concatDistinct(String[]... strArr) {
		return concat(true, strArr);
	}

	/**
	 * Concat int [ ].
	 *
	 * @param intArray the int array
	 * @return the int [ ]
	 */
	public int[] concat(int[]... intArray) {
		if (ObjectUtils.isEmpty(intArray)) {
			return IntStream.empty().toArray();
		}
		return Arrays.stream(intArray).filter(Objects::nonNull).flatMapToInt(Arrays::stream).toArray();
	}

	/**
	 * Concat long [ ].
	 *
	 * @param longArray the long array
	 * @return the long [ ]
	 */
	public long[] concat(long[]... longArray) {
		if (ObjectUtils.isEmpty(longArray)) {
			return LongStream.empty().toArray();
		}
		return Arrays.stream(longArray).filter(Objects::nonNull).flatMapToLong(Arrays::stream).toArray();
	}

	/**
	 * Concat double [ ].
	 *
	 * @param doubleArray the double array
	 * @return the double [ ]
	 */
	public double[] concat(double[]... doubleArray) {
		if (ObjectUtils.isEmpty(doubleArray)) {
			return DoubleStream.empty().toArray();
		}
		return Arrays.stream(doubleArray).filter(Objects::nonNull).flatMapToDouble(Arrays::stream).toArray();
	}


	/**
	 * Zip stream.
	 *
	 * @param <T>        the type parameter
	 * @param <R>        the type parameter
	 * @param combinator the combinator
	 * @param streams    the streams
	 * @return the stream
	 */
	@SafeVarargs
	public <T, R> Stream<R> zip(Function<Stream<T>, Stream<R>> combinator, Stream<T>... streams) {
		return ObjectUtils.isEmpty(streams) ? Stream.empty() :
			Stream.of(streams).flatMap(combinator);
	}

	/**
	 * Distinct predicate.
	 *
	 * @param <T>      the type parameter
	 * @param function the function
	 * @return the predicate
	 */
	public <T> Predicate<T> distinct(Function<? super T, ?> function) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(function.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * Convert stream.
	 *
	 * @param <T>      the type parameter
	 * @param iterator the iterator
	 * @return the stream
	 */
	public <T> Stream<T> convert(Iterator<T> iterator) {
		return StreamSupport.stream(
			Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
			false);
	}

	/**
	 * Convert stream.
	 *
	 * @param <T>         the type parameter
	 * @param enumeration the enumeration
	 * @return the stream
	 */
	public <T> Stream<T> convert(Enumeration<T> enumeration) {
		return StreamSupport.stream(
			EnumerationSpliterator.spliteratorUnknownSize(enumeration),
			false);
	}

	/**
	 * Map with index function.
	 *
	 * @param <T>        the type parameter
	 * @param <R>        the type parameter
	 * @param initValue  the init value
	 * @param biFunction the bi function
	 * @return the function
	 */
	public <T, R> Function<T, R> mapWithIndex(int initValue, BiFunction<T, Integer, R> biFunction) {
		AtomicInteger atomicInteger = new AtomicInteger(initValue);
		return t -> biFunction.apply(t, atomicInteger.getAndIncrement());
	}

	/**
	 * For each with index consumer.
	 *
	 * @param <T>        the type parameter
	 * @param initValue  the init value
	 * @param biConsumer the bi consumer
	 * @return the consumer
	 */
	public <T> Consumer<T> forEachWithIndex(int initValue, BiConsumer<T, Integer> biConsumer) {
		AtomicInteger atomicInteger = new AtomicInteger(initValue);
		return t -> biConsumer.accept(t, atomicInteger.getAndIncrement());
	}
}
