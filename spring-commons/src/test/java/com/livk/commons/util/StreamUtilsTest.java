package com.livk.commons.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * StreamUtilsTest
 * </p>
 *
 * @author livk
 */
class StreamUtilsTest {

    @Test
    void testConcatMap() {
        Map<String, List<String>> result1 = StreamUtils.concat(Map.of("username", "livk", "password", "123456"),
                Map.of("username", "root", "host", "192.168.1.1"));
        Map<String, List<String>> listMap1 = Map.of("username", List.of("livk", "root"), "password",
                List.of("123456"), "host", List.of("192.168.1.1"));
        assertEquals(listMap1, result1);

        Map<String, List<String>> result2 = StreamUtils.concat(Map.of("username", "livk", "password", "123456"),
                Map.of("username", "root", "host", "192.168.1.1"),
                Map.of("username", "admin", "host", "192.168.1.2"));
        Map<String, List<String>> listMap2 = Map.of("username", List.of("livk", "root", "admin"), "password",
                List.of("123456"), "host", List.of("192.168.1.1", "192.168.1.2"));
        assertEquals(listMap2, result2);
    }

    @Test
    void testConcatArray() {
        Integer[] result = StreamUtils.concat(new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6},
                new Integer[]{7, 8, 9}).toArray(Integer[]::new);
        Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(intArray, result);
    }

    @Test
    void testDistinct() {
        List<Integer> list = Stream.of(1, 1, 2, 2, 3, 3, 4, 4)
                .filter(StreamUtils.distinct(Function.identity()))
                .toList();
        assertEquals(list, List.of(1, 2, 3, 4));

        List<User> users = Stream.of(new User(1, "1"), new User(1, "11"),
                        new User(2, "2"), new User(2, "2"))
                .filter(StreamUtils.distinct(User::id))
                .toList();
        assertEquals(users, List.of(new User(1, "1"), new User(2, "2")));
    }

    @Test
    void of() {
        List<Integer> result = StreamUtils.of(List.of(1, 2, 3).iterator()).toList();
        assertIterableEquals(List.of(1, 2, 3), result);
    }

    private record User(Integer id, String username) {

    }
}


