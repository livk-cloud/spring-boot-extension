package com.livk.commons.spring.el;

import com.livk.commons.spring.context.SpringContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
@SpringBootTest("spring.application.root.name=livk")
class ExpressionResolverTest {

    private final static Object[] args = new Object[]{"livk"};
    private final static Map<String, String> map = Map.of("username", "livk");
    final ExpressionResolver resolver = new SpringExpressionResolver();
    final String springContextHolderName = SpringContextHolder.class.getName();
    private final Method method = this.getClass().getDeclaredMethod("parseMethod", String.class);

    ExpressionResolverTest() throws NoSuchMethodException {
    }


    @Test
    void evaluate() {
        assertTrue(resolver.evaluate("'livk'==#username", method, args, Boolean.class));
        assertEquals("livk", resolver.evaluate("#username", method, args));

        assertTrue(resolver.evaluate("'livk'==#username", map, Boolean.class));
        assertEquals("livk", resolver.evaluate("#username", map));

        assertEquals("root:livk", resolver.evaluate("root:#{#username}", method, args));
        assertEquals("root:livk:123456", resolver.evaluate("root:#{#username}:#{#password}", method, args, Map.of("password", "123456")));

        assertEquals("root:livk", resolver.evaluate("root:#{#username}", map));
        assertEquals("livk:" + System.getProperty("user.dir"),
                resolver.evaluate(("#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}"), map));

        assertEquals("livk:livk",
                resolver.evaluate("#{#username}:#{T(" + springContextHolderName + ").getProperty(\"spring.application.root.name\")}", map));

        assertEquals("root:livk", resolver.evaluate("root:#{#username}", method, args));
        assertEquals("livk", resolver.evaluate("#username", method, args));
        assertEquals("livk", resolver.evaluate("livk", method, args));

        assertEquals("root:livk:123456", resolver.evaluate("root:#{#username}:#{#password}", method, args, Map.of("password", "123456")));
        assertEquals("livk123456", resolver.evaluate("#username+#password", method, args, Map.of("password", "123456")));

        assertEquals("root:livk", resolver.evaluate("root:#{#username}", map));
        assertEquals("livk", resolver.evaluate("#username", map));
        assertEquals("livk:" + System.getProperty("user.dir"),
                resolver.evaluate("#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}", map));

        assertEquals("livk:livk",
                resolver.evaluate("#{#username}:#{T(" + springContextHolderName + ").getProperty(\"spring.application.root.name\")}", map));
    }

    @SuppressWarnings("unused")
    private void parseMethod(String username) {
    }
}
