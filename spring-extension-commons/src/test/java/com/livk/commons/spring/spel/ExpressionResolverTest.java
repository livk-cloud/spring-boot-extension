/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.spring.spel;

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
