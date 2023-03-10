package com.livk.example;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * AopExample
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class AopExample {
    public static void main(String[] args) {
        SpringLauncher.run(AopExample.class, args);
    }
}
