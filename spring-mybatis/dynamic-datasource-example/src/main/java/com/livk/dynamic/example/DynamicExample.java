package com.livk.dynamic.example;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * DynamicExample
 * </p>
 *
 * @author livk
 * @date 2022/11/21
 */
@SpringBootApplication
public class DynamicExample {
    public static void main(String[] args) {
        LivkSpring.run(DynamicExample.class, args);
    }
}
