package com.livk.example;

import com.livk.spring.LivkSpring;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * AopExample
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@Slf4j
@SpringBootApplication
public class AopExample {
    public static void main(String[] args) {
        LivkSpring.run(AopExample.class, args);
    }
}
