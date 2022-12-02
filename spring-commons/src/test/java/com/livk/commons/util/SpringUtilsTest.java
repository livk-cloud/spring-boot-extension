package com.livk.commons.util;

import com.livk.commons.util.SpringUtils;
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
 * @date 2022/8/25
 */
class SpringUtilsTest {

    private final static Object[] args = new Object[]{"livk"};
    private final static Map<String, String> map = Map.of("username", "livk");
    private final Method method = this.getClass().getDeclaredMethod("parseMethod", String.class);

    SpringUtilsTest() throws NoSuchMethodException {
    }

    @Test
    void parseSpELTestMethod4() {
        assertTrue(SpringUtils.parseSpEL(method, args, "'livk'==#username", Boolean.class));
    }

    @Test
    void parseSpELTestMethod3() {
        assertEquals("livk", SpringUtils.parseSpEL(method, args, "#username"));
    }

    @Test
    void parseSpELTestMap3() {
        assertTrue(SpringUtils.parseSpEL(map, "'livk'==#username", Boolean.class));
    }

    @Test
    void parseSpELTestMap2() {
        assertEquals("livk", SpringUtils.parseSpEL(map, "#username"));
    }

    @Test
    void parseTemplateTestMethod3() {
        assertEquals("root:livk", SpringUtils.parseTemplate(method, args, "root:#{#username}"));
    }

    @Test
    void parseTemplateTestMethod4() {
        assertEquals("root:livk:123456", SpringUtils.parseTemplate(method, args, "root:#{#username}:#{#password}", Map.of("password", "123456")));
    }

    @Test
    void testParseTemplateTestMap2() {
        assertEquals("root:livk", SpringUtils.parseTemplate(map, "root:#{#username}"));
    }

    @Test
    void parse() {
        assertEquals("root:livk", SpringUtils.parse(method, args, "root:#{#username}"));
        assertEquals("livk", SpringUtils.parse(method, args, "#username"));
    }

    @Test
    void testParse() {
        assertEquals("root:livk:123456", SpringUtils.parse(method, args, "root:#{#username}:#{#password}", Map.of("password", "123456")));
        assertEquals("livk123456", SpringUtils.parse(method, args, "#username+#password", Map.of("password", "123456")));
    }

    @Test
    void testParse1() {
        assertEquals("root:livk", SpringUtils.parse(map, "root:#{#username}"));
        assertEquals("livk", SpringUtils.parse(map, "#username"));
    }

    @SuppressWarnings("unused")
    private void parseMethod(String username) {
    }
}
