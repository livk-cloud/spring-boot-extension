/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * <p>
 * StreamUtilsTest
 * </p>
 *
 * @author livk
 */
class BaseStreamUtilsTest {

	@Test
	void testConcatMap() {
		Map<String, List<String>> result1 = BaseStreamUtils.concat(Map.of("username", "livk", "password", "123456"),
				Map.of("username", "root", "host", "192.168.1.1"));
		Map<String, List<String>> listMap1 = Map.of("username", List.of("livk", "root"), "password", List.of("123456"),
				"host", List.of("192.168.1.1"));
		assertEquals(listMap1, result1);

		Map<String, List<String>> result2 = BaseStreamUtils.concat(Map.of("username", "livk", "password", "123456"),
				Map.of("username", "root", "host", "192.168.1.1"), Map.of("username", "admin", "host", "192.168.1.2"));
		Map<String, List<String>> listMap2 = Map.of("username", List.of("livk", "root", "admin"), "password",
				List.of("123456"), "host", List.of("192.168.1.1", "192.168.1.2"));
		assertEquals(listMap2, result2);
	}

	@Test
	void testConcatArray() {
		int[] result1 = BaseStreamUtils.concat(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 }, new int[] { 7, 8, 9 });
		int[] intArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		assertArrayEquals(intArray, result1);

		long[] result2 = BaseStreamUtils.concat(new long[] { 1, 2, 3 }, new long[] { 4, 5, 6 }, new long[] { 7, 8, 9 });
		long[] longArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		assertArrayEquals(longArray, result2);

		double[] result3 = BaseStreamUtils.concat(new double[] { 1, 2, 3 }, new double[] { 4, 5, 6 },
				new double[] { 7, 8, 9 });
		double[] doubleArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		assertArrayEquals(doubleArray, result3);

		String[] result4 = BaseStreamUtils.concat(false, new String[] { "1", "2", "3" }, new String[] { "4", "5", "6" },
				new String[] { "1", "2", "3" });
		String[] stringArray = { "1", "2", "3", "4", "5", "6", "1", "2", "3" };
		assertArrayEquals(stringArray, result4);

		String[] result5 = BaseStreamUtils.concatDistinct(new String[] { "1", "2", "3" },
				new String[] { "4", "5", "6" }, new String[] { "1", "2", "3" });
		String[] stringArrayDistinct = { "1", "2", "3", "4", "5", "6" };
		assertArrayEquals(stringArrayDistinct, result5);

		Recode[] result6 = BaseStreamUtils
			.concat(new Recode[] { Recode.of("root"), Recode.of("admin") }, new Recode[] { Recode.of("guest") })
			.toArray(Recode[]::new);
		Recode[] recodeArray = { Recode.of("root"), Recode.of("admin"), Recode.of("guest") };
		assertArrayEquals(recodeArray, result6);
	}

	@Test
	void testDistinct() {
		List<Integer> list = Stream.of(1, 1, 2, 2, 3, 3, 4, 4)
			.filter(BaseStreamUtils.distinct(Function.identity()))
			.toList();
		assertEquals(list, List.of(1, 2, 3, 4));

		List<User> users = Stream.of(new User(1, "1"), new User(1, "11"), new User(2, "2"), new User(2, "2"))
			.filter(BaseStreamUtils.distinct(User::id))
			.toList();
		assertEquals(users, List.of(new User(1, "1"), new User(2, "2")));
	}

	@Test
	void mapWithIndex() {
		List<String> list = List.of("root", "livk", "admin");
		List<User> users1 = List.of(new User(0, "root"), new User(1, "livk"), new User(2, "admin"));
		List<User> result1 = list.stream().map(BaseStreamUtils.mapWithIndex(0, (s, i) -> new User(i, s))).toList();
		assertIterableEquals(users1, result1);

		List<User> users2 = List.of(new User(10, "root"), new User(11, "livk"), new User(12, "admin"));
		List<User> result2 = list.stream().map(BaseStreamUtils.mapWithIndex(10, (s, i) -> new User(i, s))).toList();
		assertIterableEquals(users2, result2);
	}

	@Test
	void forEachWithIndex() {
		List.of("root", "livk", "admin")
			.forEach(BaseStreamUtils.forEachWithIndex(0, (s, i) -> System.out.println("index:" + i + " data:" + s)));

		List.of("root", "livk", "admin")
			.forEach(BaseStreamUtils.forEachWithIndex(10, (s, i) -> System.out.println("index:" + i + " data:" + s)));
	}

	@Test
	void zip() {
		Stream<String> result = Stream.of("1", "2", "3", "4", "5", "6");
		Stream<String> zip = BaseStreamUtils.zip(stream -> stream.map(Objects::toString), Stream.of(1, 2),
				Stream.of(3, 4), Stream.of(5, 6));

		assertLinesMatch(result, zip);
	}

	@Data
	@RequiredArgsConstructor(staticName = "of")
	static class Recode {

		private final String name;

	}

	private record User(Integer id, String username) {

	}

}
