package com.livk.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * <p>
 * EnvironmentUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/10/17
 */
@SpringBootTest("spring.application.root.name=livk")
class EnvironmentUtilsTest {

    @Autowired
    Environment environment;

    @Test
    void getSubPropertiesTest() {
        Map<String, String> result = EnvironmentUtils.getSubProperties(environment, "spring.application.root");
        Assertions.assertEquals(Map.of("name", "livk"), result);
    }
}
