package com.livk.doc.webflux.example;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * WebfluxDocExample
 * </p>
 *
 * @author livk
 * @date 2022/10/19
 */
@SpringBootApplication
public class WebfluxDocExample {
    public static void main(String[] args) {
        LivkSpring.run(WebfluxDocExample.class, args);
    }
}
