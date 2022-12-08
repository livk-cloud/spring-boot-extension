package com.livk.commons.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * SpringUtilsTest
 * </p>
 *
 * @author livk
 */
class SpringUtilsTest {

    private final static Object[] args = new Object[]{"livk"};
    private final static Map<String, String> map = Map.of("username", "livk");
    private final Method method = this.getClass().getDeclaredMethod("parseMethod", String.class);

    SpringUtilsTest() throws NoSuchMethodException {
    }

    @Test
    void parseSpELTest() {
        assertTrue(SpringUtils.parseSpEL(method, args, "'livk'==#username", Boolean.class));
        assertEquals("livk", SpringUtils.parseSpEL(method, args, "#username"));

        assertTrue(SpringUtils.parseSpEL(map, "'livk'==#username", Boolean.class));
        assertEquals("livk", SpringUtils.parseSpEL(map, "#username"));
    }

    @Test
    void parseTemplateTest() {
        assertEquals("root:livk", SpringUtils.parseTemplate(method, args, "root:#{#username}"));
        assertEquals("root:livk:123456", SpringUtils.parseTemplate(method, args, "root:#{#username}:#{#password}", Map.of("password", "123456")));

        assertEquals("root:livk", SpringUtils.parseTemplate(map, "root:#{#username}"));
    }

    @Test
    void parseEverythingTest() {
        assertEquals("root:livk", SpringUtils.parseEverything(method, args, "root:#{#username}"));
        assertEquals("livk", SpringUtils.parseEverything(method, args, "#username"));
        assertEquals("livk", SpringUtils.parseEverything(method, args, "livk"));

        assertEquals("root:livk:123456", SpringUtils.parseEverything(method, args, "root:#{#username}:#{#password}", Map.of("password", "123456")));
        assertEquals("livk123456", SpringUtils.parseEverything(method, args, "#username+#password", Map.of("password", "123456")));

        assertEquals("root:livk", SpringUtils.parseEverything(map, "root:#{#username}"));
        assertEquals("livk", SpringUtils.parseEverything(map, "#username"));
    }

    @SuppressWarnings("unused")
    private void parseMethod(String username) {
    }
}
