package com.livk.commons.spring.util;

import com.livk.commons.spring.context.SpringContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * SpringUtilsTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest("spring.application.root.name=livk")
class SpringUtilsTest {

    @Autowired
    Environment environment;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private BeanFactory beanFactory;

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
        assertEquals("livk:" + System.getProperty("user.dir"),
                SpringUtils.parseTemplate(map, "#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}"));

        String springContextHolderName = SpringContextHolder.class.getName();
        assertEquals("livk:livk",
                SpringUtils.parseTemplate(map, "#{#username}:#{T(" + springContextHolderName + ").getProperty(\"spring.application.root.name\")}"));
    }

    @Test
    void parseTest() {
        assertEquals("root:livk", SpringUtils.parse(method, args, "root:#{#username}"));
        assertEquals("livk", SpringUtils.parse(method, args, "#username"));
        assertEquals("livk", SpringUtils.parse(method, args, "livk"));

        assertEquals("root:livk:123456", SpringUtils.parse(method, args, "root:#{#username}:#{#password}", Map.of("password", "123456")));
        assertEquals("livk123456", SpringUtils.parse(method, args, "#username+#password", Map.of("password", "123456")));

        assertEquals("root:livk", SpringUtils.parse(map, "root:#{#username}"));
        assertEquals("livk", SpringUtils.parse(map, "#username"));
        assertEquals("livk:" + System.getProperty("user.dir"),
                SpringUtils.parse(map, "#{#username}:#{T(java.lang.System).getProperty(\"user.dir\")}"));

        String springContextHolderName = SpringContextHolder.class.getName();
        assertEquals("livk:livk",
                SpringUtils.parse(map, "#{#username}:#{T(" + springContextHolderName + ").getProperty(\"spring.application.root.name\")}"));
    }

    @Test
    void findByAnnotationTypeTest() {
        List<String> packages = AutoConfigurationPackages.get(beanFactory);
        Set<Class<?>> result = SpringUtils.findByAnnotationType(TestAnnotation.class, resourceLoader,
                packages.toArray(String[]::new));
        assertEquals(Set.of(A.class, TestController.class), result);
    }

    @Test
    void getSubPropertiesTest() {
        Map<String, String> result = SpringUtils.getSubProperties(environment, "spring.application.root");
        assertEquals(Map.of("name", "livk"), result);
    }

    @SuppressWarnings("unused")
    private void parseMethod(String username) {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @TestAnnotation
    @Controller
    @interface TestController {

        @AliasFor(annotation = Controller.class)
        String value() default "";
    }

    @TestController
    static class A {
    }
}
