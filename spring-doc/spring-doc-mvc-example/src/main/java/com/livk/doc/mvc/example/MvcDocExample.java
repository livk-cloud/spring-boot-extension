package com.livk.doc.mvc.example;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * MvcDocExample
 * </p>
 *
 * @author livk
 * @date 2022/10/19
 */
@SpringBootApplication
public class MvcDocExample {
    public static void main(String[] args) {
        LivkSpring.run(MvcDocExample.class, args);
    }
}
