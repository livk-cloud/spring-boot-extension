/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
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

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Iterator;

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
		this.entrySplitter = Preconditions.checkNotNull(entrySplitter);
	}

	/**
	 * 静态构造器
	 * @param outerSplitter the outer splitter
	 * @param entrySplitter the entry splitter
	 * @return the multi value map splitter
	 */
	public static MultiValueMapSplitter of(Splitter outerSplitter, Splitter entrySplitter) {
		return new MultiValueMapSplitter(outerSplitter, entrySplitter);
	}

	/**
	 * 静态构造器
	 * @param outerSplitter the outer splitter
	 * @param entrySplitter the entry splitter
	 * @return the multi value map splitter
	 */
	public static MultiValueMapSplitter of(Splitter outerSplitter, String entrySplitter) {
		return new MultiValueMapSplitter(outerSplitter, Splitter.on(entrySplitter));
	}

	/**
	 * 静态构造器
	 * @param outerSplitter the outer splitter
	 * @param entrySplitter the entry splitter
	 * @return the multi value map splitter
	 */
	public static MultiValueMapSplitter of(String outerSplitter, String entrySplitter) {
		return new MultiValueMapSplitter(Splitter.on(outerSplitter), Splitter.on(entrySplitter));
	}

	/**
	 * 拆分CharSequence成一个MultiValueMap
	 * <p>
	 * 示例: String str = "root=1,2,3&amp;root=4&amp;a=b&amp;a=c"
	 * <p>
	 * MultiValueMapSplitter.of("&amp;", "=").split(str) -> {root=["1,2,3", "4"], a=["b",
	 * "c"]}
	 * @param sequence 待分割的字符串
	 * @return MultiValueMap
	 */
	public MultiValueMap<String, String> split(CharSequence sequence) {
		return split(sequence, "^$");
	}

	/**
	 * 拆分CharSequence成一个MultiValueMap
	 * <p>
	 * 示例: String str = "root=1,2,3&amp;root=4&amp;a=b&amp;a=c"
	 * <p>
	 * MultiValueMapSplitter.of("&amp;", "=").split(str,",") -> {root=["1", "2", "3",
	 * "4"], a=["b", "c"]}
	 * @param sequence 待分割的字符串
	 * @param regex value分割符
	 * @return MultiValueMap
	 */
	public MultiValueMap<String, String> split(CharSequence sequence, String regex) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		for (String entry : outerSplitter.split(sequence)) {
			Iterator<String> entryFields = entrySplitter.split(entry).iterator();

			Preconditions.checkArgument(entryFields.hasNext(), INVALID_ENTRY_MESSAGE, entry);
			String key = entryFields.next();

			Preconditions.checkArgument(entryFields.hasNext(), INVALID_ENTRY_MESSAGE, entry);

			map.addAll(key, Arrays.asList(entryFields.next().split(regex)));

			Preconditions.checkArgument(!entryFields.hasNext(), INVALID_ENTRY_MESSAGE, entry);
		}
		return CollectionUtils.unmodifiableMultiValueMap(map);
	}

}
