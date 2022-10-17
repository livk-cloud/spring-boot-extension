package com.livk.util.springutils;

import com.livk.util.SpringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * SpringUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/10/17
 */
@SpringBootTest
public class SpringUtilsTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private BeanFactory beanFactory;

    @Test
    void findByAnnotationTypeTest() {
        List<String> packages = AutoConfigurationPackages.get(beanFactory);
        Set<Class<?>> result = SpringUtils.findByAnnotationType(TestAnnotation.class, resourceLoader,
                packages.toArray(String[]::new));
        Assertions.assertIterableEquals(Set.of(A.class), result);
    }
}
