package com.livk.local.lock;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * LocalLockExample
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class LocalLockExample {
    public static void main(String[] args) {
        LivkSpring.run(LocalLockExample.class, args);
    }
}
