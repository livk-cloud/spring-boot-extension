package com.livk.dynamic.example;

import com.livk.autoconfigure.dynamic.annotation.EnableDynamicDatasource;
import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * DynamicExample
 * </p>
 *
 * @author livk
 * @date 2022/11/21
 */
@EnableDynamicDatasource
@SpringBootApplication
public class DynamicExample {
    public static void main(String[] args) {
        LivkSpring.run(DynamicExample.class, args);
    }
}
