package com.livk.local.lock;

import com.livk.commons.spring.SpringLauncher;
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
        SpringLauncher.run(LocalLockExample.class, args);
    }
}
