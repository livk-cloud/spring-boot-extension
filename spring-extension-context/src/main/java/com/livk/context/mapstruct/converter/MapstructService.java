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

package com.livk.context.mapstruct.converter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * MapstructService
 * </p>
 *
 * @author livk
 */
public interface MapstructService {

	/**
	 * Convert t.
	 * @param <S> the type parameter
	 * @param <T> the type parameter
	 * @param source the source
	 * @param targetType the target class
	 * @return the t
	 */
	<S, T> T convert(S source, Class<T> targetType);

	/**
	 * Convert stream.
	 * @param <S> the type parameter
	 * @param <T> the type parameter
	 * @param sources the sources
	 * @param targetType the target class
	 * @return the stream
	 */
	default <S, T> Stream<T> convert(Collection<S> sources, Class<T> targetType) {
		if (sources == null || sources.isEmpty()) {
			return Stream.empty();
		}
		return sources.stream().map(source -> convert(source, targetType));
	}

	/**
	 * Convert list.
	 * @param <S> the type parameter
	 * @param <T> the type parameter
	 * @param sources the sources
	 * @param targetType the target class
	 * @return the list
	 */
	default <S, T> List<T> convertList(Collection<S> sources, Class<T> targetType) {
		return convert(sources, targetType).collect(Collectors.toList());
	}

	/**
	 * Convert unmodifiable list.
	 * @param <S> the type parameter
	 * @param <T> the type parameter
	 * @param sources the sources
	 * @param targetType the target class
	 * @return the list
	 */
	default <S, T> List<T> convertUnmodifiableList(Collection<S> sources, Class<T> targetType) {
		return convert(sources, targetType).toList();
	}

	/**
	 * Convert set.
	 * @param <S> the type parameter
	 * @param <T> the type parameter
	 * @param sources the sources
	 * @param targetType the target class
	 * @return the set
	 */
	default <S, T> Set<T> convertSet(Collection<S> sources, Class<T> targetType) {
		return convert(sources, targetType).collect(Collectors.toSet());
	}

	/**
	 * Convert unmodifiable set.
	 * @param <S> the type parameter
	 * @param <T> the type parameter
	 * @param sources the sources
	 * @param targetType the target class
	 * @return the set
	 */
	default <S, T> Set<T> convertUnmodifiableSet(Collection<S> sources, Class<T> targetType) {
		return convert(sources, targetType).collect(Collectors.toUnmodifiableSet());
	}

}
