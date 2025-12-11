/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>
 * Stream工具类
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class StreamUtils {

	/**
	 * 合并Map,key相同的则合并成List
	 * @param <K> key type parameter
	 * @param <V> value type parameter
	 * @param maps maps
	 * @return map
	 */
	@SuppressWarnings("varargs")
	@SafeVarargs
	public <K, V> Map<K, List<V>> concat(Map<K, V>... maps) {
		if (ObjectUtils.isEmpty(maps)) {
			return Collections.emptyMap();
		}
		return Arrays.stream(maps)
			.filter(Objects::nonNull)
			.flatMap(map -> map.entrySet().stream())
			.collect(Collectors.groupingBy(Map.Entry::getKey,
					Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
	}

	/**
	 * 合并数组
	 * @param <T> type parameter
	 * @param ts the ts
	 * @return stream
	 */
	@SuppressWarnings("varargs")
	@SafeVarargs
	public <T> Stream<T> concat(T[]... ts) {
		if (ObjectUtils.isEmpty(ts)) {
			return Stream.empty();
		}
		return Arrays.stream(ts).filter(Objects::nonNull).flatMap(Arrays::stream);
	}

	/**
	 * 合并string数组
	 * @param distinct 是否去重
	 * @param strArr str[]
	 * @return string []
	 */
	public String[] concat(boolean distinct, String[]... strArr) {
		Stream<String> concat = concat(strArr);
		if (distinct) {
			concat = concat.distinct();
		}
		return concat.toArray(String[]::new);
	}

	/**
	 * 合并string数组并去重
	 * @param strArr str[]
	 * @return string []
	 */
	public String[] concatDistinct(String[]... strArr) {
		return concat(true, strArr);
	}

	/**
	 * 合并int数组
	 * @param intArray int array
	 * @return int []
	 */
	public int[] concat(int[]... intArray) {
		if (ObjectUtils.isEmpty(intArray)) {
			return IntStream.empty().toArray();
		}
		return Arrays.stream(intArray).filter(Objects::nonNull).flatMapToInt(Arrays::stream).toArray();
	}

	/**
	 * 合并long数组
	 * @param longArray long array
	 * @return long []
	 */
	public long[] concat(long[]... longArray) {
		if (ObjectUtils.isEmpty(longArray)) {
			return LongStream.empty().toArray();
		}
		return Arrays.stream(longArray).filter(Objects::nonNull).flatMapToLong(Arrays::stream).toArray();
	}

	/**
	 * 合并double数组
	 * @param doubleArray double array
	 * @return double []
	 */
	public double[] concat(double[]... doubleArray) {
		if (ObjectUtils.isEmpty(doubleArray)) {
			return DoubleStream.empty().toArray();
		}
		return Arrays.stream(doubleArray).filter(Objects::nonNull).flatMapToDouble(Arrays::stream).toArray();
	}

	/**
	 * 通过function合并多个Stream
	 * @param <T> 转换前泛型
	 * @param <R> 转换后泛型
	 * @param combinator 转换Function
	 * @param streams 待转换Stream
	 * @return 合并后的Stream
	 */
	@SuppressWarnings("varargs")
	@SafeVarargs
	public <T, R> Stream<R> zip(Function<Stream<T>, Stream<R>> combinator, Stream<T>... streams) {
		Assert.notNull(combinator, "combinator must not be null");
		return ObjectUtils.isEmpty(streams) ? Stream.empty()
				: Stream.of(streams).filter(Objects::nonNull).flatMap(combinator);
	}

	/**
	 * 根据某个条件进行去重
	 * @param <T> type parameter
	 * @param function 去重条件
	 * @return predicate
	 */
	public <T> Predicate<T> distinct(Function<? super T, ?> function) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(function.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * Enumeration转化成Stream
	 * @param <T> type parameter
	 * @param enumeration enumeration
	 * @return stream
	 */
	public <T> Stream<T> convert(Enumeration<T> enumeration) {
		Assert.notNull(enumeration, "enumeration must not be null");
		return StreamSupport.stream(EnumerationSpliterator.spliteratorUnknownSize(enumeration), false);
	}

	/**
	 * Stream.map()产生出index序号
	 * @param <T> type parameter
	 * @param <R> type parameter
	 * @param initValue 初始值
	 * @param biFunction bi function
	 * @return function
	 */
	public <T, R> Function<T, R> mapWithIndex(int initValue, BiFunction<T, Integer, R> biFunction) {
		Assert.notNull(biFunction, "biFunction must not be null");
		AtomicInteger atomicInteger = new AtomicInteger(initValue);
		return t -> biFunction.apply(t, atomicInteger.getAndIncrement());
	}

	/**
	 * Foreach中产生index序号
	 * @param <T> type parameter
	 * @param initValue 初始值
	 * @param biConsumer bi consumer
	 * @return consumer
	 */
	public <T> Consumer<T> forEachWithIndex(int initValue, BiConsumer<T, Integer> biConsumer) {
		Assert.notNull(biConsumer, "biConsumer must not be null");
		AtomicInteger atomicInteger = new AtomicInteger(initValue);
		return t -> biConsumer.accept(t, atomicInteger.getAndIncrement());
	}

}
