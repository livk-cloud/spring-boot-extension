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

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class StreamUtilsTests {

	@Test
	void testConcatMap() {
		Map<String, List<String>> result1 = StreamUtils.concat(Map.of("username", "livk", "password", "123456"),
				Map.of("username", "root", "host", "192.168.1.1"));
		Map<String, List<String>> listMap1 = Map.of("username", List.of("livk", "root"), "password", List.of("123456"),
				"host", List.of("192.168.1.1"));
		assertThat(result1).isEqualTo(listMap1);

		Map<String, List<String>> result2 = StreamUtils.concat(Map.of("username", "livk", "password", "123456"),
				Map.of("username", "root", "host", "192.168.1.1"), Map.of("username", "admin", "host", "192.168.1.2"));
		Map<String, List<String>> listMap2 = Map.of("username", List.of("livk", "root", "admin"), "password",
				List.of("123456"), "host", List.of("192.168.1.1", "192.168.1.2"));
		assertThat(result2).isEqualTo(listMap2);
	}

	@Test
	void testConcatArray() {
		int[] result1 = StreamUtils.concat(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 }, new int[] { 7, 8, 9 });
		int[] intArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		assertThat(result1).containsExactly(intArray);

		long[] result2 = StreamUtils.concat(new long[] { 1, 2, 3 }, new long[] { 4, 5, 6 }, new long[] { 7, 8, 9 });
		long[] longArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		assertThat(result2).containsExactly(longArray);

		double[] result3 = StreamUtils.concat(new double[] { 1, 2, 3 }, new double[] { 4, 5, 6 },
				new double[] { 7, 8, 9 });
		double[] doubleArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		assertThat(result3).containsExactly(doubleArray);

		String[] result4 = StreamUtils.concat(false, new String[] { "1", "2", "3" }, new String[] { "4", "5", "6" },
				new String[] { "1", "2", "3" });
		String[] stringArray = { "1", "2", "3", "4", "5", "6", "1", "2", "3" };
		assertThat(result4).containsExactly(stringArray);

		String[] result5 = StreamUtils.concatDistinct(new String[] { "1", "2", "3" }, new String[] { "4", "5", "6" },
				new String[] { "1", "2", "3" });
		String[] stringArrayDistinct = { "1", "2", "3", "4", "5", "6" };
		assertThat(result5).containsExactly(stringArrayDistinct);

		Recode[] result6 = StreamUtils
			.concat(new Recode[] { Recode.of("root"), Recode.of("admin") }, new Recode[] { Recode.of("guest") })
			.toArray(Recode[]::new);
		Recode[] recodeArray = { Recode.of("root"), Recode.of("admin"), Recode.of("guest") };
		assertThat(result6).containsExactly(recodeArray);
	}

	@Test
	void testDistinct() {
		List<Integer> list = Stream.of(1, 1, 2, 2, 3, 3, 4, 4)
			.filter(StreamUtils.distinct(Function.identity()))
			.toList();
		assertThat(list).containsExactly(1, 2, 3, 4);

		List<User> users = Stream.of(new User(1, "1"), new User(1, "11"), new User(2, "2"), new User(2, "2"))
			.filter(StreamUtils.distinct(User::id))
			.toList();
		assertThat(users).containsExactly(new User(1, "1"), new User(2, "2"));
	}

	@Test
	void mapWithIndex() {
		List<String> list = List.of("root", "livk", "admin");
		List<User> users1 = List.of(new User(0, "root"), new User(1, "livk"), new User(2, "admin"));
		List<User> result1 = list.stream().map(StreamUtils.mapWithIndex(0, (s, i) -> new User(i, s))).toList();
		assertThat(result1).containsExactlyElementsOf(users1);

		List<User> users2 = List.of(new User(10, "root"), new User(11, "livk"), new User(12, "admin"));
		List<User> result2 = list.stream().map(StreamUtils.mapWithIndex(10, (s, i) -> new User(i, s))).toList();
		assertThat(result2).containsExactlyElementsOf(users2);
	}

	@Test
	void forEachWithIndex() {
		// 这里断言不涉及状态变化，所以保留打印；如果需要断言，可以用Mockito等捕获调用
		List.of("root", "livk", "admin")
			.forEach(StreamUtils.forEachWithIndex(0, (s, i) -> System.out.println("index:" + i + " data:" + s)));

		List.of("root", "livk", "admin")
			.forEach(StreamUtils.forEachWithIndex(10, (s, i) -> System.out.println("index:" + i + " data:" + s)));
	}

	@Test
	void zip() {
		Stream<String> expected = Stream.of("1", "2", "3", "4", "5", "6");
		Stream<String> zipped = StreamUtils.zip(stream -> stream.map(Object::toString), Stream.of(1, 2),
				Stream.of(3, 4), Stream.of(5, 6));

		// 断言两个流元素内容相同（先收集为List断言）
		assertThat(zipped).containsExactlyElementsOf(expected.toList());
	}

	@Data
	@RequiredArgsConstructor(staticName = "of")
	static class Recode {

		private final String name;

	}

	private record User(Integer id, String username) {

	}

}
