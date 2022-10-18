package com.livk.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

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
        Assertions.assertTrue(SpringUtils.parseSpEL(method, args, "'livk'==#username", Boolean.class));
    }

    @Test
    void parseSpELTestMethod3() {
        Assertions.assertEquals("livk", SpringUtils.parseSpEL(method, args, "#username"));
    }

    @Test
    void parseSpELTestMap3() {
        Assertions.assertTrue(SpringUtils.parseSpEL(map, "'livk'==#username", Boolean.class));
    }

    @Test
    void parseSpELTestMap2() {
        Assertions.assertEquals("livk", SpringUtils.parseSpEL(map, "#username"));
    }

    @Test
    void parseTemplateTestMethod3() {
        Assertions.assertEquals("root:livk", SpringUtils.parseTemplate(method, args, "root:#{#username}"));
    }

    @Test
    void parseTemplateTestMethod4() {
        Assertions.assertEquals("root:livk:123456", SpringUtils.parseTemplate(method, args, "root:#{#username}:#{#password}", Map.of("password", "123456")));
    }

    @Test
    void testParseTemplateTestMap2() {
        Assertions.assertEquals("root:livk", SpringUtils.parseTemplate(map, "root:#{#username}"));
    }

    @Test
    void parse() {
        Assertions.assertEquals("root:livk", SpringUtils.parse(method, args, "root:#{#username}"));
        Assertions.assertEquals("livk", SpringUtils.parse(method, args, "#username"));
    }

    @Test
    void testParse() {
        Assertions.assertEquals("root:livk:123456", SpringUtils.parse(method, args, "root:#{#username}:#{#password}", Map.of("password", "123456")));
        Assertions.assertEquals("livk123456", SpringUtils.parse(method, args, "#username+#password", Map.of("password", "123456")));
    }

    @Test
    void testParse1() {
        Assertions.assertEquals("root:livk", SpringUtils.parse(map, "root:#{#username}"));
        Assertions.assertEquals("livk", SpringUtils.parse(map, "#username"));
    }

    private void parseMethod(String username) {

    }
}
