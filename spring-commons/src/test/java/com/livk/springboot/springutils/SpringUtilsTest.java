package com.livk.springboot.springutils;

import com.livk.commons.util.SpringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * SpringUtilsTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest("spring.application.root.name=livk")
public class SpringUtilsTest {

    @Autowired
    Environment environment;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private BeanFactory beanFactory;

    private final static Map<String, String> map = Map.of("username", "livk");

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

    @Test
    void parseTemplateTest() {
        assertEquals("livk:livk",
                SpringUtils.parseTemplate(map, "#{#username}:#{T(com.livk.commons.support.SpringContextHolder).getProperty(\"spring.application.root.name\")}"));
    }

    @Test
    void parseEverythingTest() {
        assertEquals("livk:livk",
                SpringUtils.parse(map, "#{#username}:#{T(com.livk.commons.support.SpringContextHolder).getProperty(\"spring.application.root.name\")}"));
    }
}
