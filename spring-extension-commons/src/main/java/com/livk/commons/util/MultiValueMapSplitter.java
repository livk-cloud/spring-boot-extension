/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.util;

import com.google.common.base.Splitter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 用于将字符串拆分成可迭代MultiValueMap对象，是线程安全不可变的
 * <p>
 * {@link com.google.common.base.Splitter.MapSplitter}的补充版
 * <p>
 * 返回数据是 {@link MultiValueMap}
 *
 * @author livk
 * @see com.google.common.base.Splitter
 * @see com.google.common.base.Splitter.MapSplitter
 * @see MultiValueMap
 */
public final class MultiValueMapSplitter {

	private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
	private final Splitter outerSplitter;
	private final Splitter entrySplitter;

	private MultiValueMapSplitter(Splitter outerSplitter, Splitter entrySplitter) {
		this.outerSplitter = outerSplitter; // only "this" is passed
		this.entrySplitter = checkNotNull(entrySplitter);
	}

	/**
	 * 静态构造器
	 *
	 * @param outerSplitter the outer splitter
	 * @param entrySplitter the entry splitter
	 * @return the multi value map splitter
	 */
	public static MultiValueMapSplitter of(Splitter outerSplitter, Splitter entrySplitter) {
		return new MultiValueMapSplitter(outerSplitter, entrySplitter);
	}

	/**
	 * 静态构造器
	 *
	 * @param outerSplitter the outer splitter
	 * @param entrySplitter the entry splitter
	 * @return the multi value map splitter
	 */
	public static MultiValueMapSplitter of(Splitter outerSplitter, String entrySplitter) {
		return new MultiValueMapSplitter(outerSplitter, Splitter.on(entrySplitter));
	}

	/**
	 * 静态构造器
	 *
	 * @param outerSplitter the outer splitter
	 * @param entrySplitter the entry splitter
	 * @return the multi value map splitter
	 */
	public static MultiValueMapSplitter of(String outerSplitter, String entrySplitter) {
		return new MultiValueMapSplitter(Splitter.on(outerSplitter), Splitter.on(entrySplitter));
	}

	/**
	 * 拆分CharSequence成一个MultiValueMap
	 *
	 * @param sequence the sequence
	 * @return the multi value map
	 * @throws IllegalArgumentException if the specified sequence does not split into valid map                                  entries, or if there are duplicate keys
	 */
	public MultiValueMap<String, String> split(CharSequence sequence) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		for (String entry : outerSplitter.split(sequence)) {
			Iterator<String> entryFields = entrySplitter.split(entry).iterator();

			checkArgument(entryFields.hasNext(), INVALID_ENTRY_MESSAGE, entry);
			String key = entryFields.next();

			checkArgument(entryFields.hasNext(), INVALID_ENTRY_MESSAGE, entry);
			String value = entryFields.next();
			map.add(key, value);

			checkArgument(!entryFields.hasNext(), INVALID_ENTRY_MESSAGE, entry);
		}
		return CollectionUtils.unmodifiableMultiValueMap(map);
	}
}
